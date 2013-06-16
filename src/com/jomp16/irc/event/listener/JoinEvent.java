/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.event.listener;

import com.jomp16.irc.IRCManager;
import com.jomp16.irc.channel.Channel;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.user.User;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class JoinEvent extends CommandEvent {
    public JoinEvent(IRCManager ircManager, User user, Channel channel, String message, Event event, ArrayList<String> args, Logger log) {
        super(ircManager, user, channel, message, event, args, log);
    }
}
