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
import com.jomp16.irc.event.Level;
import com.jomp16.irc.plugin.help.HelpRegister;
import com.jomp16.irc.user.User;
import com.jomp16.irc.user.UserDAO;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CommandEvent {
    private static HashMap<String, Integer> spamLock = new HashMap<>();
    private IRCManager ircManager;
    private User user;
    private UserDAO userDAO;
    private Channel channel;
    private ChannelDAO channelDAO;
    private String message;
    private ArrayList<String> args;
    private Logger log;
    private Event event;
    private Date date;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ss");

    public CommandEvent(IRCManager ircManager, User user, Channel channel, String message, Event event, ArrayList<String> args, Logger log) {
        this.ircManager = ircManager;
        this.user = user;
        this.channel = channel;
        this.channelDAO = new ChannelDAO(ircManager, user, channel);
        this.message = message;
        this.event = event;
        this.args = args;
        this.log = log;
        date = new Date(System.currentTimeMillis());
    }

    public int transform(int i) {
        if (i >> 31 != 0) {
            i = i * -1;
        }

        if (i > 60) {
            return 60;
        }

        return i;
    }

    private boolean isLocked() {
        int currentSec = Integer.parseInt(simpleDateFormat.format(date));
        if (spamLock.containsKey(this.user.getUserName())) {
            int timeLock = spamLock.get(this.user.getUserName());
            int timeOut = ircManager.getConfiguration().getCommandLock();
            if (timeLock > transform(currentSec + timeOut) || timeLock < transform(currentSec - timeOut)) {
                spamLock.replace(this.user.getUserName(), currentSec);
                return false;
            }
        } else {
            if (user.getLevel() == Level.ADMIN || user.getLevel() == Level.MOD) {
                return false;
            } else {
                spamLock.put(this.user.getUserName(), currentSec);
                return false;
            }
        }
        return true;
    }

    public void respond(Object message) {
        if (!isLocked()) {
            ircManager.getOutputIRC().sendMessage(channel.getTargetName(), user.getUserName(), message);
        }
    }

    public void respond(Object message, boolean showName) {
        if (!isLocked()) {
            if (showName) {
                respond(message);
            } else {
                ircManager.getOutputIRC().sendMessage(channel.getTargetName(), message);
            }
        }
    }

    public void respond(Object target, Object message) {
        if (!isLocked()) {
            ircManager.getOutputIRC().sendMessage(target, message);
        }
    }

    public void respond(Object target, Object user, Object message) {
        if (!isLocked()) {
            ircManager.getOutputIRC().sendMessage(target, user, message);
        }
    }

    public void respond(String user, Object message) {
        if (!isLocked()) {
            ircManager.getOutputIRC().sendMessage(channel.getTargetName(), user, message);
        }
    }

    public void showUsage(String command) {
        for (HelpRegister helpRegister : event.getHelpRegister()) {
            if (helpRegister.getCommand().equals(command)) {
                respond("Usage: " + ircManager.getConfiguration().getPrefix() + helpRegister.getUsage());
            }
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

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public ChannelDAO getChannelDAO() {
        return channelDAO;
    }
}
