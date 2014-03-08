/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.event.listener.event;

import joptsimple.OptionSet;
import org.apache.logging.log4j.Logger;
import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.channel.Channel;
import tk.jomp16.irc.channel.ChannelDAO;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.Listener;
import tk.jomp16.irc.user.User;
import tk.jomp16.plugin.PluginInfo;
import tk.jomp16.plugin.help.HelpRegister;

import java.util.List;

public class CommandEvent extends Listener {
    private OptionSet optionSet;
    private String message;
    private String rawMessage;
    private List<String> args;
    private String command;

    public CommandEvent(IRCManager ircManager, User user, Channel channel, ChannelDAO channelDAO, Logger log, PluginInfo pluginInfo) {
        super(ircManager, user, channel, channelDAO, log, pluginInfo);
    }

    public CommandEvent(IRCManager ircManager, User user, Channel channel, ChannelDAO channelDAO, Logger log) {
        super(ircManager, user, channel, channelDAO, log);
    }

    public synchronized void showUsage(Event event, String command) {
        for (HelpRegister helpRegister : event.getHelpRegister()) {
            if (helpRegister.getCommand().equals(command)) {
                String usage = helpRegister.getUsage();

                if (usage != null) {
                    respond("Usage: " + ircManager.getConfiguration().getPrefix() + command + " " + helpRegister.getUsage());
                } else {
                    respond("Usage: " + ircManager.getConfiguration().getPrefix());
                }
            } else {
                if (helpRegister.getOptCommands() != null && helpRegister.getOptCommands().length != 0) {
                    for (String s : helpRegister.getOptCommands()) {
                        if (s.equals(command)) {
                            String usage = helpRegister.getUsage();
                            if (usage != null) {
                                respond("Usage: " + ircManager.getConfiguration().getPrefix() + s + " " + helpRegister.getUsage());
                            } else {
                                respond("Usage: " + ircManager.getConfiguration().getPrefix() + s);
                            }
                        }
                    }
                }
            }
        }

        try {
            wait(700);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    public OptionSet getOptionSet() {
        return optionSet;
    }

    public void setOptionSet(OptionSet optionSet) {
        this.optionSet = optionSet;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
