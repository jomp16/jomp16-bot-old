/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.plugin;

import org.apache.commons.lang3.StringUtils;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.events.PrivMsgEvent;
import tk.jomp16.irc.event.listener.event.CommandEvent;
import tk.jomp16.irc.event.listener.event.DisableEvent;
import tk.jomp16.irc.event.listener.event.ResetEvent;
import tk.jomp16.logger.LogManager;
import tk.jomp16.plugin.commands.Commands;
import tk.jomp16.plugin.help.Help;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ConstantConditions")
@Deprecated // since I rewrote plugin handling, this class will not work as intended
public class Plugin extends Event {
    private static Map<String, Event> eventHashMap = new HashMap<>();

    //@Command(value = "plugin", level = Level.OWNER)
    public void plugin(CommandEvent commandEvent) throws Exception {
        if (eventHashMap.size() == 0) {
            loadPluginInfo(commandEvent.getIrcManager().getEvents());
        }

        if (commandEvent.getArgs().size() >= 1) {
            switch (commandEvent.getArgs().get(0)) {
                case "disable":
                    if (commandEvent.getArgs().size() >= 2) {
                        if (eventHashMap.containsKey(commandEvent.getArgs().get(1))) {
                            Event eventToDisable = eventHashMap.get(commandEvent.getArgs().get(1));

                            eventToDisable.onDisable(new DisableEvent(commandEvent.getIrcManager(), LogManager.getLogger(eventToDisable.getClass())));
                            eventHashMap.remove(eventToDisable.getClass().getName());
                            commandEvent.getIrcManager().getEvents().remove(eventToDisable);

                            PrivMsgEvent.reloadEvents();

                            commandEvent.respond("Disabled plugin");
                        } else {
                            commandEvent.respond("Plugin not found");
                        }
                    }
                    break;
                case "reload":
                    if (commandEvent.getArgs().size() >= 2) {
                        if (commandEvent.getArgs().get(1).equals("all")) {
                            int tmp = 0;

                            commandEvent.getIrcManager().getEvents().clear();
                            commandEvent.getIrcManager().getPluginLoader().closeAll();
                            eventHashMap.clear();

                            for (Event event : commandEvent.getIrcManager().getPluginLoader().load()) {
                                tmp++;

                                commandEvent.getIrcManager().registerEvent(event, false);
                            }

                            commandEvent.getIrcManager().getEvents().addAll(commandEvent.getIrcManager().getBundledEvent());
                            loadPluginInfo(commandEvent.getIrcManager().getEvents());
                            PrivMsgEvent.reloadEvents();
                            Help.reloadHelp();
                            Commands.reload();

                            commandEvent.respond("Reloaded " + tmp + " plugin classes");
                        } else {
                            if (eventHashMap.containsKey(commandEvent.getArgs().get(1))) {
                                if (!commandEvent.getIrcManager().getBundledEvent().contains(eventHashMap.get(commandEvent.getArgs().get(1)))) {
                                    Event eventToDisable = eventHashMap.get(commandEvent.getArgs().get(1));
                                    try {
                                        eventToDisable.onDisable(new DisableEvent(commandEvent.getIrcManager(), LogManager.getLogger(eventToDisable.getClass())));
                                    } catch (Exception e) {
                                        // Ignore it...
                                        commandEvent.getLog().error(e);
                                    }
                                    commandEvent.getIrcManager().getEvents().remove(eventToDisable);
                                    eventHashMap.remove(eventToDisable.getClass().getSimpleName());

                                    File pluginFile = pluginFile(commandEvent.getArgs().get(1));
                                    int tmp = 0;

                                    if (pluginFile != null) {
                                        for (Event event : commandEvent.getIrcManager().getPluginLoader().load(pluginFile)) {
                                            tmp++;

                                            commandEvent.getIrcManager().registerEvent(event, false);
                                            eventHashMap.put(event.getClass().getSimpleName(), event);
                                        }

                                        PrivMsgEvent.reloadEvents();

                                        commandEvent.respond("Reloaded " + tmp + " plugin classes");
                                    } else {
                                        commandEvent.respond("Plugin doesn't exists");
                                    }
                                }
                            } else {
                                commandEvent.respond("Plugin doesn't exists");
                            }
                        }
                    }
                    break;
                case "showall":
                    List<String> pluginNameTmp = new ArrayList<>();

                    for (String pluginName : eventHashMap.keySet()) {
                        pluginNameTmp.add(pluginName);
                    }

                    commandEvent.respond("Available plugins: " + StringUtils.join(pluginNameTmp, ", "));
                    break;
                case "load":
                    if (commandEvent.getArgs().size() >= 2) {
                        File pluginFile = pluginFile(commandEvent.getArgs().get(1));
                        if (pluginFile != null) {
                            int tmp = 0;

                            for (Event event : commandEvent.getIrcManager().getPluginLoader().load(pluginFile)) {
                                tmp++;

                                commandEvent.getIrcManager().registerEvent(event, false);
                                eventHashMap.put(event.getClass().getSimpleName(), event);
                            }

                            PrivMsgEvent.reloadEvents();

                            commandEvent.respond("Loaded " + tmp + " plugin classes");
                        } else {
                            commandEvent.respond("Plugin doesn't exists");
                        }
                    }
                    break;
                case "reset":
                    if (commandEvent.getArgs().size() >= 2) {
                        if (eventHashMap.containsKey(commandEvent.getArgs().get(1))) {
                            eventHashMap.get(commandEvent.getArgs().get(1)).onReset(new ResetEvent(commandEvent.getIrcManager(), LogManager.getLogger(commandEvent.getArgs().get(1))));
                            PrivMsgEvent.reloadEvents();
                        } else {
                            commandEvent.respond("Plugin not found");
                        }
                    }
                    break;
            }
        }
    }

    private File pluginFile(String jarName) {
        File f = new File(System.getProperty("user.dir").replace("\\", " ") + "/plugins");
        for (File file : f.listFiles()) {
            if (file.getName().equals(jarName + ".jar")) {
                return file;
            }
        }
        return null;
    }

    @Override
    public void onReset(ResetEvent resetEvent) throws Exception {
        eventHashMap.clear();
        loadPluginInfo(resetEvent.getIrcManager().getEvents());
    }

    private void loadPluginInfo(List<Event> events) {
        for (Event event : events) {
            eventHashMap.put(event.getClass().getSimpleName(), event);
        }
    }
}
