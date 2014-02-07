/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.event.listener;

import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.channel.Channel;
import tk.jomp16.irc.channel.ChannelDAO;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.plugin.help.HelpRegister;
import tk.jomp16.irc.user.User;
import tk.jomp16.logger.Logger;

import java.util.List;

public class CommandEvent {
    private IRCManager ircManager;
    private User user;
    //    private UserDAO userDAO;
    private Channel channel;
    private ChannelDAO channelDAO;
    private String message;
    private String rawMessage;
    private List<String> args;
    private Logger log;
    private String command;

    public CommandEvent(IRCManager ircManager, User user, Channel channel, String message, String rawMessage, String command, List<String> args, Logger log) {
        this.ircManager = ircManager;
        this.user = user;
        this.channel = channel;
        this.channelDAO = new ChannelDAO(ircManager, channel);
        this.message = message;
        this.rawMessage = rawMessage;
        this.command = command;
        this.args = args;
        this.log = log;
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
                respond("Usage: " + ircManager.getConfiguration().getPrefix() + command + " " + helpRegister.getUsage());
            } else {
                if (helpRegister.getOptCommands() != null && helpRegister.getOptCommands().length != 0) {
                    for (String s : helpRegister.getOptCommands()) {
                        if (s.equals(command)) {
                            respond("Usage: " + ircManager.getConfiguration().getPrefix() + s + " " + helpRegister.getUsage());
                        }
                    }
                }
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

    public String getRawMessage() {
        return rawMessage;
    }

    public String getCommand() {
        return command;
    }

    public List<String> getArgs() {
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
