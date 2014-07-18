/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.cyanogenmod;

import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.event.CommandEvent;
import tk.jomp16.plugin.cyanogenmod.changelog.Changelog;
import tk.jomp16.plugin.cyanogenmod.device.Device;
import tk.jomp16.plugin.cyanogenmod.download.Download;
import tk.jomp16.plugin.cyanogenmod.supported.Supported;

public class CyanogenMod extends Event {
    private Device device = new Device();
    private Download download = new Download();
    private Changelog changelog = new Changelog();
    private Supported supported = new Supported();

    @Command(value = "cyanogenmod", optCommands = "cm", args = {"device::", "changelog:", "download:", "supported::", "all", "version:"})
    public void cyanogenmod(CommandEvent commandEvent) throws Exception {
        commandEvent.getArgs().remove(0);

        if (commandEvent.getOptionSet().has("device")) {
            commandEvent.setMessage(commandEvent.getMessage().replace("-device", "")
                    .replace("--device", ""));
            device.device(commandEvent);
        } else if (commandEvent.getOptionSet().has("changelog")) {
            commandEvent.setMessage(commandEvent.getMessage().replace("-changelog", "")
                    .replace("--changelog", ""));
            changelog.changelog(commandEvent);
        } else if (commandEvent.getOptionSet().has("download")) {
            commandEvent.setMessage(commandEvent.getMessage().replace("-download", "")
                    .replace("--download", ""));
            download.download(commandEvent);
        } else if (commandEvent.getOptionSet().has("supported")) {
            commandEvent.setMessage(commandEvent.getMessage().replace("-supported", "")
                    .replace("--supported", ""));
            supported.supported(commandEvent);
        }
    }
}
