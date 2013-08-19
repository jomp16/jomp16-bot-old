/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.plugin.cyanogenmod;

import com.jomp16.irc.event.CommandFilter;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.listener.CommandEvent;
import com.jomp16.irc.event.listener.InitEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

public class Devices extends Event {
    private HashMap<String, Versions> devices = new HashMap<>();
    private String CM_WIKI_URL = "http://wiki.cyanogenmod.org/w/%s_Info";

    @CommandFilter("device")
    public void device(CommandEvent commandEvent) throws Exception {
        if (commandEvent.getArgs().size() >= 1) {
            if (commandEvent.getArgs().get(0).equals("all")) {
                commandEvent.respond("Since the length of message is really big, the function is disabled for now, see the devices at http://wiki.cyanogenmod.org/w/Devices");

                //String tmp = StringUtils.join(devices.keySet(), ", ");

                //System.out.println(tmp.length());

                //commandEvent.respond("Officially supported devices from CyanogenMod: " + tmp);
            } else if (commandEvent.getArgs().get(0).equals("info")) {
                if (commandEvent.getArgs().size() >= 2) {
                    if (devices.containsKey(commandEvent.getArgs().get(1))) {
                        commandEvent.respond("Device codename: " + commandEvent.getArgs().get(1) + " | " +
                                "Current branch is: " + devices.get(commandEvent.getArgs().get(1)).value + " | " +
                                "CM Wiki: " + String.format(CM_WIKI_URL, getDeviceNameUpperCased(commandEvent.getArgs().get(1))));
                    } else {
                        commandEvent.respond("No officially supported device found");
                    }
                }
            }
        }
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        URL url = new URL("https://raw.github.com/CyanogenMod/hudson/master/cm-build-targets");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String tmp;

            while ((tmp = reader.readLine()) != null) {
                if (!tmp.startsWith("#") && tmp.length() != 0) {
                    String[] splitted = tmp.split(" ");

                    String fullDeviceName = splitted[0];
                    fullDeviceName = fullDeviceName.substring(3);
                    int index = fullDeviceName.indexOf('-');
                    String deviceName = fullDeviceName.substring(0, index);

                    Versions version = Versions.getVersionByBranch(splitted[1]);

                    devices.put(deviceName, version);
                }
            }
        }
    }

    private String getDeviceNameUpperCased(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
