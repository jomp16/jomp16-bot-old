/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.parser.parsers;

import com.jomp16.irc.IRCManager;
import com.jomp16.irc.channel.ChannelLevel;
import com.jomp16.irc.channel.ChannelList;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.parser.Parser;
import com.jomp16.irc.parser.ParserToken;

public class NamesParser extends Parser {
    @Override
    public Event parse(IRCManager ircManager, ParserToken token) {
        // :hobana.freenode.net 353 jomp16-bot = #jomp16-bot :jomp16-bot jomp16 nebkat RossWell98 @ChanServ
        // [jomp16-bot, =, #jomp16-bot, jomp16-bot jomp16 nebkat RossWell98 @ChanServ]

        String channel = token.getParams().get(2);
        String[] users = token.getParams().get(3).split(" ");

        for (String s : users) {
            switch (s.charAt(0)) {
                case '@':
                    ChannelList.addUserToChannel(channel, s.substring(1), ChannelLevel.OP);
                    break;
                case '+':
                    ChannelList.addUserToChannel(channel, s.substring(1), ChannelLevel.VOICE);
                    break;
                default:
                    ChannelList.addUserToChannel(channel, s, ChannelLevel.NORMAL);
                    break;
            }
        }

        return null;
    }
}
