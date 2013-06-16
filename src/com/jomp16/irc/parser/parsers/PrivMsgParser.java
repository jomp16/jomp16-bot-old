/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.parser.parsers;

import com.jomp16.irc.IRCManager;
import com.jomp16.irc.channel.Channel;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.Level;
import com.jomp16.irc.event.events.PrivMsgEvent;
import com.jomp16.irc.parser.Parser;
import com.jomp16.irc.parser.ParserToken;
import com.jomp16.irc.user.User;

public class PrivMsgParser extends Parser {
    @Override
    public Event parse(IRCManager ircManager, long time, ParserToken token) {
        User user;

        if (ircManager.getAdmins().contains(token.getSource().getUser() + "@" + token.getSource().getHost())) {
            user = new User(token.getSource().getNick(), token.getSource().getUser(), token.getSource().getHost(), Level.ADMIN);
        } else if (ircManager.getMods().contains(token.getSource().getUser() + "@" + token.getSource().getHost())) {
            user = new User(token.getSource().getNick(), token.getSource().getUser(), token.getSource().getHost(), Level.NORMAL);
        } else {
            user = new User(token.getSource().getNick(), token.getSource().getUser(), token.getSource().getHost(), Level.NORMAL);
        }
        Channel channel;

        if (token.getParams().get(0).startsWith("#")) {
            channel = new Channel(token.getParams().get(0), null, null, null, null);
        } else {
            channel = new Channel(user.getUserName(), null, null, null, null);
        }

        String message = token.getParams().get(1);
        token.getParams().remove(0);
        new PrivMsgEvent(ircManager, user, channel, message, ircManager.getEvents());
        return null;
    }
}
