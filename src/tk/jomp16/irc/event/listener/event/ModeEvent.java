/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.event.listener.event;

import org.apache.logging.log4j.Logger;
import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.channel.Channel;
import tk.jomp16.irc.channel.ChannelDAO;
import tk.jomp16.irc.event.listener.Listener;
import tk.jomp16.irc.user.User;
import tk.jomp16.plugin.PluginInfo;

public class ModeEvent extends Listener {
    private String userModed;
    private tk.jomp16.irc.event.events.ModeEvent.Modes mode;

    public ModeEvent(IRCManager ircManager, User user, Channel channel, ChannelDAO channelDAO, Logger log, PluginInfo pluginInfo) {
        super(ircManager, user, channel, channelDAO, log, pluginInfo);
    }

    public ModeEvent(IRCManager ircManager, User user, Channel channel, ChannelDAO channelDAO, Logger log) {
        super(ircManager, user, channel, channelDAO, log);
    }


    public String getUserModed() {
        return userModed;
    }

    public void setUserModed(String userModed) {
        this.userModed = userModed;
    }

    public tk.jomp16.irc.event.events.ModeEvent.Modes getMode() {
        return mode;
    }

    public void setMode(tk.jomp16.irc.event.events.ModeEvent.Modes mode) {
        this.mode = mode;
    }
}
