/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.event.listener.event;

import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.channel.Channel;
import tk.jomp16.irc.channel.ChannelDAO;
import tk.jomp16.irc.event.listener.Listener;
import tk.jomp16.irc.user.User;
import tk.jomp16.logger.Logger;
import tk.jomp16.plugin.PluginInfo;

import java.util.List;

public class PrivMsgEvent extends Listener {
    private String message;
    private tk.jomp16.irc.event.events.PrivMsgEvent.PrivMSGTag tag;
    private List<String> args;

    public PrivMsgEvent(IRCManager ircManager, User user, Channel channel, ChannelDAO channelDAO, Logger log, PluginInfo pluginInfo) {
        super(ircManager, user, channel, channelDAO, log, pluginInfo);
    }

    public PrivMsgEvent(IRCManager ircManager, User user, Channel channel, ChannelDAO channelDAO, Logger log) {
        super(ircManager, user, channel, channelDAO, log);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public tk.jomp16.irc.event.events.PrivMsgEvent.PrivMSGTag getTag() {
        return tag;
    }

    public void setTag(tk.jomp16.irc.event.events.PrivMsgEvent.PrivMSGTag tag) {
        this.tag = tag;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }
}
