/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.plugin.plugin;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import tk.jomp16.irc.event.CommandFilter;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.Level;
import tk.jomp16.irc.event.events.PrivMsgEvent;
import tk.jomp16.irc.event.listener.CommandEvent;
import tk.jomp16.irc.event.listener.DisableEvent;
import tk.jomp16.irc.event.listener.ResetEvent;
import tk.jomp16.irc.plugin.PluginLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("ConstantConditions")
public class Plugin extends Event {
    private static HashMap<String, Event> eventHashMap = new HashMap<>();

    @CommandFilter(value = "plugin", level = Level.OWNER)
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

                            eventToDisable.onDisable(new DisableEvent(commandEvent.getIrcManager(), LogManager.getLogger(eventToDisable.getClass().getSimpleName())));
                            eventHashMap.remove(eventToDisable.getClass().getSimpleName());
                            commandEvent.getIrcManager().getEvents().remove(eventToDisable);

                            PrivMsgEvent.reloadEvents(commandEvent.getIrcManager().getEvents());

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
                            eventHashMap.clear();

                            for (Event event : new PluginLoader().load()) {
                                tmp++;

                                commandEvent.getIrcManager().registerEvent(event, false);
                            }

                            commandEvent.getIrcManager().getEvents().addAll(commandEvent.getIrcManager().getBundledEvent());

                            loadPluginInfo(commandEvent.getIrcManager().getEvents());

                            PrivMsgEvent.reloadEvents(commandEvent.getIrcManager().getEvents());

                            commandEvent.respond("Reloaded " + tmp + " plugin classes");
                        } else {
                            if (eventHashMap.containsKey(commandEvent.getArgs().get(1))) {
                                if (!commandEvent.getIrcManager().getBundledEvent().contains(eventHashMap.get(commandEvent.getArgs().get(1)))) {
                                    Event eventToDisable = eventHashMap.get(commandEvent.getArgs().get(1));
                                    try {
                                        eventToDisable.onDisable(new DisableEvent(commandEvent.getIrcManager(), LogManager.getLogger(eventToDisable.getClass().getSimpleName())));
                                    } catch (Exception e) {
                                        // Ignore it...
                                        commandEvent.getLog().error(e);
                                    }
                                    commandEvent.getIrcManager().getEvents().remove(eventToDisable);
                                    eventHashMap.remove(eventToDisable.getClass().getSimpleName());

                                    File pluginFile = pluginFile(commandEvent.getArgs().get(1));
                                    int tmp = 0;

                                    if (pluginFile != null) {
                                        for (Event event : new PluginLoader().load(pluginFile)) {
                                            tmp++;

                                            commandEvent.getIrcManager().registerEvent(event, false);
                                            eventHashMap.put(event.getClass().getSimpleName(), event);
                                        }

                                        PrivMsgEvent.reloadEvents(commandEvent.getIrcManager().getEvents());

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
                    ArrayList<String> pluginNameTmp = new ArrayList<>();

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

                            for (Event event : new PluginLoader().load(pluginFile)) {
                                tmp++;

                                commandEvent.getIrcManager().registerEvent(event, false);
                                eventHashMap.put(event.getClass().getSimpleName(), event);
                            }

                            PrivMsgEvent.reloadEvents(commandEvent.getIrcManager().getEvents());

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
                            PrivMsgEvent.reloadEvents(commandEvent.getIrcManager().getEvents());
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

    private void loadPluginInfo(ArrayList<Event> events) {
        for (Event event : events) {
            eventHashMap.put(event.getClass().getSimpleName(), event);
        }
    }
}