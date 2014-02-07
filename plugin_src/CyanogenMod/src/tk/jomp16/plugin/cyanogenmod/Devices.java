/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.cyanogenmod;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.CommandEvent;
import tk.jomp16.irc.event.listener.InitEvent;
import tk.jomp16.irc.plugin.help.HelpRegister;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Devices extends Event {
    private Map<String, DeviceInfo> devices = new HashMap<>();
    private String CM_WIKI_URL = "http://wiki.cyanogenmod.org/w/%s_Info";
    private String BUILD_TARGET_URL = "https://raw.github.com/CyanogenMod/hudson/master/cm-build-targets";

    @Command("device")
    public void device(CommandEvent commandEvent) throws Exception {
        if (commandEvent.getArgs().size() >= 1) {
            if (commandEvent.getArgs().get(0).equals("all")) {
                commandEvent.respond("Since the length of message is really big, the function is disabled for now, see the devices at http://wiki.cyanogenmod.org/w/Devices");
                commandEvent.respond("But CM has " + devices.size() + " officially supported devices =D");

                //String tmp = StringUtils.join(devices.keySet(), ", ");
                //System.out.println(tmp.length());
                //commandEvent.respond("Officially supported devices from CyanogenMod: " + tmp);
            } else if (commandEvent.getArgs().get(0).equals("version")) {
                if (commandEvent.getArgs().size() >= 2) {
                    DeviceInfo deviceInfo = devices.get(commandEvent.getArgs().get(1));

                    if (deviceInfo != null) {
                        commandEvent.respond("Device: " + deviceInfo.deviceName + " | " +
                                "CM Version: " + deviceInfo.cmVersion.getName() + " - " +
                                deviceInfo.cmVersion.getAndroidVersion());
                    }
                }
            } else {
                DeviceInfo info = devices.get(commandEvent.getArgs().get(0));

                if (info != null) {
                    commandEvent.respond("Device codename: " + info.codename + " | " +
                            "Name: " + info.deviceName + " | " +
                            "CM Wiki: " + info.cmWiki);
                } else {
                    commandEvent.respond("No officially supported device found");
                }
            }
        } else {
            commandEvent.showUsage(this, commandEvent.getCommand());
        }
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        initEvent.addHelp(this, new HelpRegister("device", "Get device info and Android version", "all or <version|info> device"));

        initEvent.getLog().info("Wait much time! Because it parses from a file and load the device name from CMWiki...");

        URL url = new URL(BUILD_TARGET_URL);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String tmp;

            while ((tmp = reader.readLine()) != null) {
                if (!tmp.startsWith("#") && tmp.length() != 0) {
                    String[] splitted = tmp.split(" ");

                    String fullDeviceName = splitted[0];
                    fullDeviceName = fullDeviceName.substring(3);
                    int index = fullDeviceName.indexOf('-');

                    String codeName = fullDeviceName.substring(0, index);

                    initEvent.getLog().info("Getting device name for: " + codeName);

                    String cmWiki = String.format(CM_WIKI_URL, getDeviceNameUpperCased(codeName));
                    String deviceName = getDeviceNameByCMWiki(cmWiki);

                    if (deviceName == null) {
                        deviceName = codeName;
                    }

                    Versions version = Versions.getVersionByBranch(splitted[1]);

                    DeviceInfo deviceInfo = new DeviceInfo(codeName, deviceName, version, cmWiki);

                    devices.put(codeName, deviceInfo);
                }
            }
        }

        initEvent.getLog().info("Loaded " + devices.size() + " devices!");
    }

    private String getDeviceNameUpperCased(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    private String getDeviceNameByCMWiki(String url) throws Exception {
        try {
            Document document = Jsoup.connect(url).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.10 Safari/537.36").referrer("http://www.google.com").followRedirects(true).get().normalise();
            String deviceNameRaw = document.title().replace("Information: ", "");

            return deviceNameRaw.substring(0, deviceNameRaw.indexOf(" (\""));
        } catch (Exception e) {
            return null;
        }
    }

    private static class DeviceInfo {
        private String codename;
        private String deviceName;
        private Versions cmVersion;
        private String cmWiki;

        private DeviceInfo(String codename, String deviceName, Versions cmVersion, String cmWiki) {
            this.codename = codename;
            this.deviceName = deviceName;
            this.cmVersion = cmVersion;
            this.cmWiki = cmWiki;
        }
    }
}
