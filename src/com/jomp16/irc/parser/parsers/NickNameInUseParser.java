/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.parser.parsers;

import com.jomp16.irc.IRCManager;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.events.NickNameInUseEvent;
import com.jomp16.irc.parser.Parser;
import com.jomp16.irc.parser.ParserToken;

public class NickNameInUseParser extends Parser {
    @Override
    public Event parse(IRCManager ircManager, long time, ParserToken token) {
        return new NickNameInUseEvent();
    }
}