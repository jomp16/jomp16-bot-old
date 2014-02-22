/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import tk.jomp16.configuration.Configuration;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.events.PrivMsgEvent;
import tk.jomp16.irc.event.listener.event.InitEvent;
import tk.jomp16.irc.output.OutputIRC;
import tk.jomp16.irc.output.OutputRaw;
import tk.jomp16.irc.parser.Parser;
import tk.jomp16.logger.LogManager;
import tk.jomp16.logger.Logger;
import tk.jomp16.plugin.PluginInfo;
import tk.jomp16.plugin.PluginLoader;
import tk.jomp16.plugin.PluginManager;
import tk.jomp16.plugin.about.About;
import tk.jomp16.plugin.commands.Commands;
import tk.jomp16.plugin.help.Help;
import tk.jomp16.plugin.plugin.Plugin;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class IRCManager {
    private List<Event> events = new ArrayList<>();
    private Multimap<String, Event> eventMultimap = HashMultimap.create();
    private Map<String, PluginInfo> pluginInfoHashMap = new HashMap<>();
    private List<Event> bundledEvent = new ArrayList<>();
    private List<String> owners = new ArrayList<>();
    private List<String> admins = new ArrayList<>();
    private List<String> mods = new ArrayList<>();
    private Socket ircSocket;
    private BufferedWriter ircWriter;
    private BufferedReader ircReader;
    private OutputRaw outputRaw;
    private OutputIRC outputIRC;
    private Configuration configuration;
    private IRCManager ircManager;
    private Logger log = LogManager.getLogger(this.getClass());
    private boolean ready = false;
    private boolean mode = false;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private PluginLoader pluginLoader;
    private PluginManager pluginManager;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public IRCManager(Configuration configuration) {
        this.configuration = configuration;
        ircManager = this;

        Runtime.getRuntime().addShutdownHook(new Thread(ircManager::shutdown));

        pluginLoader = new PluginLoader();
        pluginManager = new PluginManager();

        File f = new File("plugins");

        if (!f.exists()) {
            f.mkdir();
        }

        //loadPlugin();
        loadPlugin1();

        registerEvent(new About(), true);
        registerEvent(new Help(), true);
        registerEvent(new Commands(), true);
        registerEvent(new Plugin(), true);
    }

    public void shutdown() {
        try {
            outputIRC.quit();

            executor.shutdown();
            executor.awaitTermination(2, TimeUnit.SECONDS);

            ircWriter.close();
            ircReader.close();
            ircSocket.close();
        } catch (Exception e) {
            log.error(e);
        }
    }

    public synchronized void connect() throws Exception {
        PrivMsgEvent.ircManager = this;
        PrivMsgEvent.reloadEvents();

        executor.execute(this::connect1);

        do {
            wait(500);
        } while (!ready);
    }

    private void loadPlugin() {
        try {
            for (Event event : pluginLoader.load()) {
                registerEvent(event, false);
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void loadPlugin1() {
        try {
            pluginManager.loadAll();
            pluginManager.getPlugins().forEach(this::registerPluginEvent);
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void registerEvent(Event event, boolean bundledEvent) {
        events.add(event);
        eventMultimap.put(event.getClass().getSimpleName(), event);

        if (bundledEvent) {
            this.bundledEvent.add(event);
        }

        Runnable runnable = () -> {
            try {
                event.onInit(new InitEvent(this, LogManager.getLogger(event.getClass())));
            } catch (Exception e) {
                log.error(e);
            }
        };

        executor.execute(runnable);
    }

    public void registerPluginEvent(tk.jomp16.plugin.Plugin plugin) {
        events.addAll(plugin.getEvents());
        eventMultimap.putAll(plugin.getPluginInfo().getName(), plugin.getEvents());
        pluginInfoHashMap.put(plugin.getPluginInfo().getName(), plugin.getPluginInfo());

        Runnable runnable = () -> plugin.getEvents().forEach(event -> {
            try {
                event.onInit(new InitEvent(this, LogManager.getLogger(event.getClass())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.execute(runnable);
    }

    public void addOwner(String owner) {
        owners.add(owner);
    }

    public void addAdmin(String admin) {
        admins.add(admin);
    }

    public void addMod(String mod) {
        mods.add(mod);
    }

    public OutputRaw getOutputRaw() {
        return outputRaw;
    }

    public OutputIRC getOutputIRC() {
        return outputIRC;
    }

    public List<Event> getEvents() {
        return events;
    }

    public Multimap<String, Event> getEventMultimap() {
        return eventMultimap;
    }

    public Map<String, PluginInfo> getPluginInfoHashMap() {
        return pluginInfoHashMap;
    }

    public List<Event> getBundledEvent() {
        return bundledEvent;
    }

    public List<String> getOwners() {
        return owners;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public List<String> getMods() {
        return mods;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public BufferedWriter getIrcWriter() {
        return ircWriter;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public String getConnectedIRCCHost() {
        return Parser.getHost().substring(1);
    }

    public PluginLoader getPluginLoader() {
        return pluginLoader;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    private boolean isNickServEnabled() {
        return configuration.getPassword() != null && !configuration.getPassword().equals("null");
    }

    private void connect1() {
        try {
            ircSocket = new Socket(configuration.getServer(), configuration.getPort());
            ircWriter = new BufferedWriter(new OutputStreamWriter(ircSocket.getOutputStream()));
            ircReader = new BufferedReader(new InputStreamReader(ircSocket.getInputStream()));
            outputRaw = new OutputRaw(ircManager);
            outputIRC = new OutputIRC(ircManager);

            outputRaw.writeRaw("NICK " + configuration.getNick());
            outputRaw.writeRaw("USER " + configuration.getIdentify() + " 8 * :" + configuration.getRealName());

            String tmp;
            while ((tmp = ircReader.readLine()) != null) {
                if (tmp.contains("You have 30 seconds to identify to your nickname before it is changed.") &&
                        isNickServEnabled()) {
                    outputIRC.sendMessage("NickServ", "identify " + configuration.getPassword());
                } else if (tmp.contains("is not a registered nickname")) {
                    log.info("I couldn't identify for: " + configuration.getNick() + "!");
                    ready = true;
                } else if (tmp.contains("MODE " + configuration.getNick())) {
                    mode = true;
                }

                if (mode) {
                    ready = true;
                }

                try {
                    Parser.parseLine(ircManager, tmp);
                } catch (Exception e) {
                    log.error(e);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
}
