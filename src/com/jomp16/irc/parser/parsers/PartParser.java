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
import com.jomp16.irc.channel.ChannelList;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.events.PartEvent;
import com.jomp16.irc.parser.Parser;
import com.jomp16.irc.parser.ParserToken;
import com.jomp16.irc.user.User;

public class PartParser extends Parser {
    @Override
    public Event parse(IRCManager ircManager, ParserToken token) {
        // :jomp16!~jomp16@unaffiliated/jomp16 PART #jomp16-bot :"Saindo"
        // [#jomp16-bot, "Saindo"]
        // Source{raw='jomp16!~jomp16@unaffiliated/jomp16', nick='jomp16', user='~jomp16', host='unaffiliated/jomp16'}

        User user = new User(token.getSource().getNick(), token.getSource().getUser(), token.getSource().getHost(), Source.loopMask(ircManager, token.getSource().getRaw()));
        Channel channel = new Channel(token.getParams().get(0));
        String reason = token.getParams().get(1);

        ChannelList.removeUserToChannel(channel.getTargetName(), user.getUserName());

        return new PartEvent(ircManager, user, channel, reason);
    }
}
