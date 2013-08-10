/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc;

import com.jomp16.configuration.Configuration;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.events.PrivMsgEvent;
import com.jomp16.irc.event.listener.InitEvent;
import com.jomp16.irc.output.OutputIRC;
import com.jomp16.irc.output.OutputRaw;
import com.jomp16.irc.parser.Parser;
import com.jomp16.irc.plugin.PluginLoader;
import com.jomp16.irc.plugin.about.About;
import com.jomp16.irc.plugin.commands.Commands;
import com.jomp16.irc.plugin.help.Help;
import com.jomp16.irc.plugin.plugin.Plugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class IRCManager {
    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<Event> bundledEvent = new ArrayList<>();
    private ArrayList<String> owners = new ArrayList<>();
    private ArrayList<String> admins = new ArrayList<>();
    private ArrayList<String> mods = new ArrayList<>();
    private BufferedWriter ircWriter;
    private OutputRaw outputRaw;
    private OutputIRC outputIRC;
    private Configuration configuration;
    private IRCManager ircManager;
    private Logger log = LogManager.getLogger(this.getClass().getSimpleName());
    private boolean ready = false;

    public IRCManager(Configuration configuration) {
        this.configuration = configuration;
        ircManager = this;

        loadPlugin();

        registerEvent(new About(), true);
        registerEvent(new Help(), true);
        registerEvent(new Commands(), true);
        registerEvent(new Plugin(), true);
    }

    public synchronized void connect() throws Exception {
        PrivMsgEvent.reloadEvents(events);

        log.info("Loaded " + events.size() + " plugins classes and " + PrivMsgEvent.getEventRegisters().size() + " commands");

        new Thread(new Connect()).start();

        while (!ready) {
            wait(100);
        }
    }

    private void loadPlugin() {
        try {
            for (Event event : new PluginLoader().load()) {
                registerEvent(event, false);
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void registerEvent(Event event, boolean bundledEvent) {
        events.add(event);

        if (bundledEvent) {
            this.bundledEvent.add(event);
        }

        try {
            event.onInit(new InitEvent(this, LogManager.getLogger(event.getClass().getSimpleName())));
        } catch (Exception e) {
            log.error(e);
        }
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

    public ArrayList<Event> getEvents() {
        return events;
    }

    public ArrayList<Event> getBundledEvent() {
        return bundledEvent;
    }

    public ArrayList<String> getOwners() {
        return owners;
    }

    public ArrayList<String> getAdmins() {
        return admins;
    }

    public ArrayList<String> getMods() {
        return mods;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public BufferedWriter getIrcWriter() {
        return ircWriter;
    }

    private class Connect implements Runnable {
        @Override
        public void run() {
            try {
                Socket ircSocket = new Socket(configuration.getServer(), configuration.getPort());
                ircWriter = new BufferedWriter(new OutputStreamWriter(ircSocket.getOutputStream()));
                BufferedReader ircReader = new BufferedReader(new InputStreamReader(ircSocket.getInputStream()));
                outputRaw = new OutputRaw(ircManager);
                outputIRC = new OutputIRC(ircManager);
                outputRaw.writeRaw("NICK " + configuration.getNick());
                outputRaw.writeRaw("USER " + configuration.getIdentify() + " 8 * :" + configuration.getRealName());

                String tmp;
                while ((tmp = ircReader.readLine()) != null) {
                    if (tmp.contains("MODE " + configuration.getNick())) {
                        ready = true;
                    }
                    try {
                        Parser.parseLine(ircManager, tmp);
                    } catch (Exception e) {
                        log.error(e);
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                log.error(e);
            }
        }
    }
}
