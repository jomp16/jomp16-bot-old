/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.dispatcher;

import com.jomp16.irc.IRCManager;
import com.jomp16.irc.channel.Channel;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.listener.CommandEvent;
import com.jomp16.irc.user.User;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;

public class Dispatcher {
    public static void dispatchEvent(Event event, IRCManager ircManager, String message, User user, Channel channel, ArrayList<String> args) {
        event.respond(new CommandEvent(ircManager, user, channel, message, args, LogManager.getLogger(event.getClass().getSimpleName())));
    }
}
