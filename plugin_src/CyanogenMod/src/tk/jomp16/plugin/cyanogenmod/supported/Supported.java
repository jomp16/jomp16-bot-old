/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.cyanogenmod.supported;

import org.apache.commons.lang3.StringUtils;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.event.CommandEvent;
import tk.jomp16.plugin.cyanogenmod.device.Device;
import tk.jomp16.plugin.cyanogenmod.device.OEM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Supported extends Event {
    private String oemString;

    public Supported() {
        List<String> oemList = new ArrayList<>();

        for (OEM oem : OEM.values()) {
            oemList.add(oem.getOem());
        }

        Collections.sort(oemList, String::compareTo);

        oemString = StringUtils.join(oemList, ", ");
    }

    public void supported(CommandEvent commandEvent) {
        if (commandEvent.getMessage().length() > 0) {
            OEM oem = OEM.getOemByName(commandEvent.getMessage());

            if (oem != null) {
                List<String> devices = Device.getDevices()
                        .entrySet()
                        .parallelStream()
                        .filter(entry -> entry.getValue().getOem() != null && entry.getValue().getOem().equals(oem))
                        .map(entry -> entry.getValue().getCodename())
                        .collect(Collectors.toList());

                Collections.sort(devices, String::compareTo);

                commandEvent.respond("Device: " + StringUtils.join(devices, ", "));
            } else {
                commandEvent.respond("OEM not found");
            }
        } else {
            commandEvent.respond("Supported OEMs: " + oemString);
        }
    }
}
