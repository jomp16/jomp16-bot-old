/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.event.events;

import com.jomp16.irc.IRCManager;
import com.jomp16.irc.channel.Channel;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.user.User;
import org.apache.logging.log4j.LogManager;

public class JoinEvent extends Event {
    private IRCManager ircManager;
    private User user;
    private Channel channel;

    public JoinEvent(IRCManager ircManager, User user, Channel channel) {
        this.ircManager = ircManager;
        this.user = user;
        this.channel = channel;
    }

    @Override
    public void respond() {
        for (Event event : ircManager.getEvents()) {
            event.onJoin(new com.jomp16.irc.event.listener.event.JoinEvent(ircManager, user, channel, LogManager.getLogger(event.getClass().getSimpleName())));
        }
    }
}
