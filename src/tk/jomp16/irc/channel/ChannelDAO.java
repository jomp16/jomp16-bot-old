/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.channel;

import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.user.User;

public class ChannelDAO {
    private IRCManager ircManager;
    private Channel channel;

    public ChannelDAO(IRCManager ircManager, Channel channel) {
        this.ircManager = ircManager;
        this.channel = channel;
    }

    public synchronized void partChannel() {
        ircManager.getOutputIRC().partChannel(channel.getTargetName());
    }

    public synchronized void partChannel(String reason) {
        ircManager.getOutputIRC().partChannel(channel.getTargetName(), reason);
    }

    public synchronized void giveOP(User user) {
        ircManager.getOutputIRC().giveOP(channel.getTargetName(), user.getUserName());
    }

    public synchronized void giveVoice(User user) {
        ircManager.getOutputIRC().giveVoice(channel.getTargetName(), user.getUserName());
    }

    public synchronized void removeOP(User user) {
        ircManager.getOutputIRC().removeOP(channel.getTargetName(), user.getUserName());
    }

    public synchronized void removeVoice(User user) {
        ircManager.getOutputIRC().removeVoice(channel.getTargetName(), user.getUserName());
    }
}
