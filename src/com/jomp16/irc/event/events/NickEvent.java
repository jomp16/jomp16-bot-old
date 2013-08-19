/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.event.events;

import com.jomp16.irc.IRCManager;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.user.User;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;

public class NickEvent extends Event {
    private IRCManager ircManager;
    private User user;
    private ArrayList<String> args;

    public NickEvent(IRCManager ircManager, User user, ArrayList<String> args) {
        this.ircManager = ircManager;
        this.user = user;
        this.args = args;
    }

    @Override
    public void respond() {
        for (Event event : ircManager.getEvents()) {
            event.onNick(new com.jomp16.irc.event.listener.event.NickEvent(ircManager, user, args.get(0), args.get(1), LogManager.getLogger(event.getClass().getSimpleName())));
        }
    }
}
