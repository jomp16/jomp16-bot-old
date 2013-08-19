/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.parser.parsers;

import com.jomp16.irc.IRCManager;
import com.jomp16.irc.Source;
import com.jomp16.irc.channel.ChannelList;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.events.NickEvent;
import com.jomp16.irc.parser.Parser;
import com.jomp16.irc.parser.ParserToken;
import com.jomp16.irc.user.User;

import java.util.ArrayList;

public class NickParser extends Parser {
    @Override
    public Event parse(IRCManager ircManager, ParserToken token) {
        // [DEBUG #NICK]: :jomp16_1!~jomp16@177.134.198.245 NICK :jomp16_
        // [DEBUG #NICK]: [jomp16_]
        // [DEBUG #NICK]: Source{raw='jomp16_1!~jomp16@177.134.198.245', nick='jomp16_1', user='~jomp16', host='177.134.198.245'}

        User user = new User(token.getParams().get(0), token.getSource().getUser(), token.getSource().getHost(), Source.loopMask(ircManager, token.getSource().getRaw()));

        ArrayList<String> args = new ArrayList<>();
        args.add(token.getSource().getNick());
        args.add(token.getParams().get(0));

        ChannelList.changeNick(args.get(0), args.get(1));

        return new NickEvent(ircManager, user, args);
    }
}
