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
    private User user;
    private Channel channel;

    public ChannelDAO(IRCManager ircManager, User user, Channel channel) {
        this.ircManager = ircManager;
        this.user = user;
        this.channel = channel;
    }

    public synchronized void partChannel() {
        ircManager.getOutputRaw().writeRaw("PART " + channel.getTargetName());
    }

    public synchronized void partChannel(String reason) {
        ircManager.getOutputRaw().writeRaw("PART " + channel.getTargetName() + " :" + reason);
    }

    public synchronized void giveOP(User user) {
        ircManager.getOutputRaw().writeRaw("MODE " + channel.getTargetName() + " +o " + user.getUserName());
    }

    public synchronized void giveVoice(User user) {
        ircManager.getOutputRaw().writeRaw("MODE " + channel.getTargetName() + " +v " + user.getUserName());
    }

    public synchronized void removeOP(User user) {
        ircManager.getOutputRaw().writeRaw("MODE " + channel.getTargetName() + " -o " + user.getUserName());
    }

    public synchronized void removeVoice(User user) {
        ircManager.getOutputRaw().writeRaw("MODE " + channel.getTargetName() + " -v " + user.getUserName());
    }
}
