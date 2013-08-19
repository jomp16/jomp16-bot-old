/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.parser.parsers;

import com.jomp16.irc.IRCManager;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.events.PingEvent;
import com.jomp16.irc.parser.Parser;
import com.jomp16.irc.parser.ParserToken;

public class PingParser extends Parser {
    @Override
    public Event parse(IRCManager ircManager, ParserToken token) {
        return new PingEvent(ircManager, token.getParams().get(0));
    }
}
