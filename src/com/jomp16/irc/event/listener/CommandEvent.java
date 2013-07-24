/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.event.listener;

import com.jomp16.irc.IRCManager;
import com.jomp16.irc.channel.Channel;
import com.jomp16.irc.channel.ChannelDAO;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.plugin.help.HelpRegister;
import com.jomp16.irc.user.User;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CommandEvent {
    private static HashMap<String, Integer> spamLock = new HashMap<>();
    private IRCManager ircManager;
    private User user;
    //    private UserDAO userDAO;
    private Channel channel;
    private ChannelDAO channelDAO;
    private String message;
    private ArrayList<String> args;
    private Logger log;
    private Date date;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ss");

    public CommandEvent(IRCManager ircManager, User user, Channel channel, String message, ArrayList<String> args, Logger log) {
        this.ircManager = ircManager;
        this.user = user;
        this.channel = channel;
        this.channelDAO = new ChannelDAO(ircManager, user, channel);
        this.message = message;
        this.args = args;
        this.log = log;
        date = new Date(System.currentTimeMillis());
    }

    public synchronized void respond(Object message) {
        ircManager.getOutputIRC().sendMessage(channel.getTargetName(), user.getUserName(), message);

        try {
            wait(700);
        } catch (Exception e) {
            log.error(e);
        }
    }

    public synchronized void respond(Object message, boolean showName) {
        if (showName) {
            respond(message);
        } else {
            ircManager.getOutputIRC().sendMessage(channel.getTargetName(), message);
        }

        try {
            wait(700);
        } catch (Exception e) {
            log.error(e);
        }
    }

    public synchronized void respond(Object target, Object message) {
        ircManager.getOutputIRC().sendMessage(target, message);

        try {
            wait(700);
        } catch (Exception e) {
            log.error(e);
        }
    }

    public synchronized void respond(Object target, Object user, Object message) {
        ircManager.getOutputIRC().sendMessage(target, user, message);

        try {
            wait(700);
        } catch (Exception e) {
            log.error(e);
        }
    }

    public synchronized void respond(String user, Object message) {
        ircManager.getOutputIRC().sendMessage(channel.getTargetName(), user, message);

        try {
            wait(700);
        } catch (Exception e) {
            log.error(e);
        }
    }

    public synchronized void showUsage(Event event, String command) {
        for (HelpRegister helpRegister : event.getHelpRegister()) {
            if (helpRegister.getCommand().equals(command)) {
                respond("Usage: " + ircManager.getConfiguration().getPrefix() + helpRegister.getUsage());
            }
        }

        try {
            wait(700);
        } catch (Exception e) {
            log.error(e);
        }
    }

    public IRCManager getIrcManager() {
        return ircManager;
    }

    public User getUser() {
        return user;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<String> getArgs() {
        return args;
    }

    public Logger getLog() {
        return log;
    }

//    public UserDAO getUserDAO() {
//        return userDAO;
//    }

    public ChannelDAO getChannelDAO() {
        return channelDAO;
    }
}
