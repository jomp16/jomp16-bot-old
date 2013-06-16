/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.bot.event;

import com.jomp16.irc.event.CommandFilter;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.Level;
import com.jomp16.irc.event.listener.CommandEvent;
import com.jomp16.irc.event.listener.DisableEvent;
import com.jomp16.irc.event.listener.InitEvent;
import com.jomp16.irc.event.listener.ReloadEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.HashMap;

public class PluginEvent extends Event {
    private static HashMap<String, Event> eventHashMap = new HashMap<>();

    @CommandFilter(value = "plugin", level = Level.ADMIN)
    public void plugin(CommandEvent commandEvent) throws Exception {
        if (commandEvent.getArgs().size() >= 1) {
            switch (commandEvent.getArgs().get(0)) {
                case "disable":
                    if (commandEvent.getArgs().size() >= 2) {
                        if (eventHashMap.containsKey(commandEvent.getArgs().get(1))) {
                            eventHashMap.get(commandEvent.getArgs().get(1)).onDisable(new DisableEvent(commandEvent.getIrcManager(), LogManager.getLogger(commandEvent.getArgs().get(1))));
                            eventHashMap.remove(commandEvent.getArgs().get(1));
                        } else {
                            commandEvent.respond("Plugin not found");
                        }
                    }
                    break;
                case "reload":
                    if (commandEvent.getArgs().size() >= 2) {
                        if (eventHashMap.containsKey(commandEvent.getArgs().get(1))) {
                            eventHashMap.get(commandEvent.getArgs().get(1)).onReload(new ReloadEvent(commandEvent.getIrcManager(), LogManager.getLogger(commandEvent.getArgs().get(1))));
                        } else {
                            commandEvent.respond("Plugin not found");
                        }
                    }
                    break;
                case "showall":
                    ArrayList<String> pluginNameTmp = new ArrayList<>();

                    for (String pluginName : eventHashMap.keySet()) {
                        pluginNameTmp.add(pluginName);
                    }

                    commandEvent.respond("Available plugins: " + StringUtils.join(pluginNameTmp, ", "));
                    break;
            }
        }
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        for (Event event : initEvent.getIrcManager().getEvents()) {
            eventHashMap.put(event.getClass().getSimpleName(), event);
        }
    }
}
