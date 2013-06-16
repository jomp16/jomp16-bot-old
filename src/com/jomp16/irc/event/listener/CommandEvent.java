/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.event.listener;

import com.jomp16.help.Help;
import com.jomp16.irc.IRCManager;
import com.jomp16.irc.channel.Channel;
import com.jomp16.irc.channel.ChannelDAO;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.user.User;
import com.jomp16.irc.user.UserDAO;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class CommandEvent {
    private IRCManager ircManager;
    private User user;
    private UserDAO userDAO;
    private Channel channel;
    private ChannelDAO channelDAO;
    private String message;
    private ArrayList<String> args;
    private Logger log;
    private Event event;

    public CommandEvent(IRCManager ircManager, User user, Channel channel, String message, Event event, ArrayList<String> args, Logger log) {
        this.ircManager = ircManager;
        this.user = user;
        this.channel = channel;
        this.channelDAO = new ChannelDAO(ircManager, user, channel);
        this.message = message;
        this.event = event;
        this.args = args;
        this.log = log;
    }

    public void respond(Object message) {
        ircManager.getOutputIRC().sendMessage(channel.getTargetName(), user.getUserName(), message);
    }

    public void respond(Object message, boolean showName) {
        if (showName) {
            respond(message);
        } else {
            ircManager.getOutputIRC().sendMessage(channel.getTargetName(), message);
        }
    }

    public void respond(Object target, Object message) {
        ircManager.getOutputIRC().sendMessage(target, message);
    }

    public void respond(Object target, Object user, Object message) {
        ircManager.getOutputIRC().sendMessage(target, user, message);
    }

    public void respond(String user, Object message) {
        ircManager.getOutputIRC().sendMessage(channel.getTargetName(), user, message);
    }

    public void showUsage(String command) {
        for (Help help : event.getHelp()) {
            if (help.getCommand().equals(command)) {
                respond("Usage: " + ircManager.getConfiguration().getPrefix() + help.getUsage());
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
