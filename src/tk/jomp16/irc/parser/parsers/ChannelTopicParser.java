/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.parser.parsers;

import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.channel.ChannelList;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.parser.Parser;
import tk.jomp16.irc.parser.ParserToken;

public class ChannelTopicParser extends Parser {
    @Override
    public Event parse(IRCManager ircManager, ParserToken token) {
        String channel = token.getParams().get(1);
        String topic = token.getParams().get(2);

        ChannelList.setTopic(channel, topic);

        return null;
    }
}
