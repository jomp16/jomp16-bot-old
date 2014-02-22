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

public class KickEvent extends Listener {
    private String userKicked;
    private String reason;

    public KickEvent(IRCManager ircManager, User user, Channel channel, ChannelDAO channelDAO, Logger log, PluginInfo pluginInfo) {
        super(ircManager, user, channel, channelDAO, log, pluginInfo);
    }

    public KickEvent(IRCManager ircManager, User user, Channel channel, ChannelDAO channelDAO, Logger log) {
        super(ircManager, user, channel, channelDAO, log);
    }

    public String getUserKicked() {
        return userKicked;
    }

    public void setUserKicked(String userKicked) {
        this.userKicked = userKicked;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
