/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.plugin;

import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.Level;
import tk.jomp16.irc.event.listener.event.CommandEvent;

public class Plugin extends Event {
    @Command(value = "plugin", level = Level.OWNER, args = {"load:", "reload:", "unload:"})
    public void plugin(CommandEvent commandEvent) throws Exception {
        if (commandEvent.getOptionSet().has("load")) {
            if (commandEvent.getIrcManager().getPluginManager().loadPlugin((String) commandEvent.getOptionSet().valueOf("load"))) {
                commandEvent.respond("Success!");
            } else {
                commandEvent.respond("Something went wrong!");
            }
        } else if (commandEvent.getOptionSet().has("reload")) {
            if (commandEvent.getIrcManager().getPluginManager().reloadPlugin((String) commandEvent.getOptionSet().valueOf("reload"))) {
                commandEvent.respond("Success!");
            } else {
                commandEvent.respond("Something went wrong!");
            }
        } else if (commandEvent.getOptionSet().has("unload")) {
            if (commandEvent.getIrcManager().getPluginManager().unloadPlugin((String) commandEvent.getOptionSet().valueOf("unload"))) {
                commandEvent.respond("Success!");
            } else {
                commandEvent.respond("Something went wrong!");
            }
        }
    }
}
