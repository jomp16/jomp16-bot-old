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

public class NickEvent extends Listener {
    private String oldNick;
    private String newNick;

    public NickEvent(IRCManager ircManager, User user, Channel channel, ChannelDAO channelDAO, Logger log, PluginInfo pluginInfo) {
        super(ircManager, user, channel, channelDAO, log, pluginInfo);
    }

    public NickEvent(IRCManager ircManager, User user, Channel channel, ChannelDAO channelDAO, Logger log) {
        super(ircManager, user, channel, channelDAO, log);
    }


    public String getOldNick() {
        return oldNick;
    }

    public void setOldNick(String oldNick) {
        this.oldNick = oldNick;
    }

    public String getNewNick() {
        return newNick;
    }

    public void setNewNick(String newNick) {
        this.newNick = newNick;
    }
}
