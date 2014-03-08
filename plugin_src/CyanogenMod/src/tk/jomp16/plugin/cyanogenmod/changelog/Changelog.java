/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.cyanogenmod.changelog;

import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.event.CommandEvent;
import tk.jomp16.plugin.cyanogenmod.device.Device;
import tk.jomp16.plugin.cyanogenmod.device.DeviceInfo;

public class Changelog extends Event {
    private final String CHANGELOG_URL = "http://changelog.bbqdroid.org/#/%s/next";

    public void changelog(CommandEvent commandEvent) {
        if (commandEvent.getArgs().size() >= 1) {
            DeviceInfo deviceInfo = Device.getDevices().get(commandEvent.getArgs().get(0));

            if (deviceInfo != null) {
                commandEvent.respond("Changelog for " + deviceInfo.getCodename() + ": " + String.format(CHANGELOG_URL, deviceInfo.getCodename()));
            }
        }
    }
}
