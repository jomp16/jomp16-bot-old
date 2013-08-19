/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.parser.parsers;

import com.jomp16.irc.IRCManager;
import com.jomp16.irc.Source;
import com.jomp16.irc.channel.Channel;
import com.jomp16.irc.channel.ChannelLevel;
import com.jomp16.irc.channel.ChannelList;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.events.JoinEvent;
import com.jomp16.irc.parser.Parser;
import com.jomp16.irc.parser.ParserToken;
import com.jomp16.irc.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JoinParser extends Parser {
    private Logger log = LogManager.getLogger(this.getClass().getSimpleName());

    @Override
    public Event parse(IRCManager ircManager, ParserToken token) {
        if (token.getSource().getNick().equals(ircManager.getConfiguration().getNick())) {
            return null;
        }

        User user = new User(token.getSource().getNick(), token.getSource().getUser(), token.getSource().getHost(), Source.loopMask(ircManager, token.getSource().getRaw()));

        Channel channel;

        if (token.getParams().get(0).startsWith("#")) {
            channel = new Channel(token.getParams().get(0));
        } else {
            return null;
        }

        ChannelList.addUserToChannel(channel.getTargetName(), user.getUserName(), ChannelLevel.NORMAL);

        return new JoinEvent(ircManager, user, channel);
    }
}
