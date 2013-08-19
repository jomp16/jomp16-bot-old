/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.event.listener.event;

import com.jomp16.irc.IRCManager;
import com.jomp16.irc.channel.Channel;
import com.jomp16.irc.user.User;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class PrivMsgEvent {
    private IRCManager ircManager;
    private User user;
    private Channel channel;
    private String message;
    private ArrayList<String> args;
    private Logger logger;

    public PrivMsgEvent(IRCManager ircManager, User user, Channel channel, String message, ArrayList<String> args, Logger logger) {
        this.ircManager = ircManager;
        this.user = user;
        this.channel = channel;
        this.message = message;
        this.args = args;
        this.logger = logger;
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

    public Logger getLogger() {
        return logger;
    }

    public synchronized void respond(Object message) {
        ircManager.getOutputIRC().sendMessage(channel.getTargetName(), user.getUserName(), message);

        try {
            wait(700);
        } catch (Exception e) {
            logger.error(e);
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
            logger.error(e);
        }
    }

    public synchronized void respond(Object target, Object message) {
        ircManager.getOutputIRC().sendMessage(target, message);

        try {
            wait(700);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public synchronized void respond(Object target, Object user, Object message) {
        ircManager.getOutputIRC().sendMessage(target, user, message);

        try {
            wait(700);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public synchronized void respond(String user, Object message) {
        ircManager.getOutputIRC().sendMessage(channel.getTargetName(), user, message);

        try {
            wait(700);
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
