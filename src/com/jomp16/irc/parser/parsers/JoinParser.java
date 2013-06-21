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
import com.jomp16.irc.event.listener.JoinEvent;
import com.jomp16.irc.parser.Parser;
import com.jomp16.irc.parser.ParserToken;
import com.jomp16.irc.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JoinParser extends Parser {
    private Logger log = LogManager.getLogger(this.getClass().getSimpleName());

    @Override
    public Event parse(IRCManager ircManager, long time, ParserToken token) {
        if (token.getSource().getNick().equals(ircManager.getConfiguration().getNick())) {
            return null;
        }

        User user;
        String tmpCompleteUserMask = token.getSource().getUser() + "@" + token.getSource().getHost();

        if (ircManager.getOwners().contains(tmpCompleteUserMask)) {
            user = new User(token.getSource().getNick(), token.getSource().getUser(), token.getSource().getHost(), Level.OWNER);
        } else if (ircManager.getAdmins().contains(tmpCompleteUserMask)) {
            user = new User(token.getSource().getNick(), token.getSource().getUser(), token.getSource().getHost(), Level.ADMIN);
        } else if (ircManager.getMods().contains(tmpCompleteUserMask)) {
            user = new User(token.getSource().getNick(), token.getSource().getUser(), token.getSource().getHost(), Level.MOD);
        } else {
            user = new User(token.getSource().getNick(), token.getSource().getUser(), token.getSource().getHost(), Level.NORMAL);
        }

        Channel channel;

        if (token.getParams().get(0).startsWith("#")) {
            channel = new Channel(token.getParams().get(0));
        } else {
            return null;
        }

        token.getParams().remove(0);
        for (Event event : ircManager.getEvents()) {
            try {
                event.onJoin(new JoinEvent(ircManager, user, channel, null, event, null, LogManager.getLogger(event.getClass().getSimpleName())));
            } catch (Exception e) {
                log.error(e);
            }
        }
        return null;
    }
}
