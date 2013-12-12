/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.event.listener.event;

import org.apache.logging.log4j.Logger;
import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.channel.Channel;
import tk.jomp16.irc.user.User;

import java.util.ArrayList;

import static tk.jomp16.irc.event.events.PrivMsgEvent.PrivMSGTag;

public class PrivMsgEvent {
    private IRCManager ircManager;
    private User user;
    private Channel channel;
    private String message;
    private PrivMSGTag tag;
    private ArrayList<String> args;
    private Logger logger;

    public PrivMsgEvent(IRCManager ircManager, User user, Channel channel, String message, PrivMSGTag tag, ArrayList<String> args, Logger logger) {
        this.ircManager = ircManager;
        this.user = user;
        this.channel = channel;
        this.message = message;
        this.tag = tag;
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

    public PrivMSGTag getTag() {
        return tag;
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
