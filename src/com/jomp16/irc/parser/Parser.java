/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.parser;

import com.jomp16.irc.IRCManager;
import com.jomp16.irc.Source;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.listener.CommandEvent;
import com.jomp16.irc.parser.parsers.JoinParser;
import com.jomp16.irc.parser.parsers.NickNameInUseParser;
import com.jomp16.irc.parser.parsers.PingPongParser;
import com.jomp16.irc.parser.parsers.PrivMsgParser;
import com.jomp16.irc.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class Parser {
    private static final HashMap<Tags, Parser> parsers = new HashMap<>();
    private static Logger log = LogManager.getLogger(Parser.class.getSimpleName());
    private static String host = null;

    static {
        parsers.put(Tags.COMMAND_PING, new PingPongParser());
        parsers.put(Tags.COMMAND_PRIVMSG, new PrivMsgParser());
        parsers.put(Tags.COMMAND_JOIN, new JoinParser());
        parsers.put(Tags.ERROR_NICK_IN_USE, new NickNameInUseParser());
    }

    public static void parse(IRCManager ircManager, String line, boolean verbose) {
        String[] parts = Utils.splitUntilOccurenceAfterOccurence(line, " ", ":", 1, " ", line.startsWith(":") ? 1 : 0);
        long time = System.currentTimeMillis();

        if (verbose) {
            log.info(Arrays.toString(parts));
        }

        if (host == null) {
            host = parts[0];
        }

        if (parts.length < 1 || (parts[0].startsWith(":") && parts.length < 2)) {
            if (verbose) {
                log.error("Line too short: " + line);
            }
            return;
        }

        if (!parts[0].startsWith(":")) {
            String[] modifiedParts = new String[parts.length + 1];
            System.arraycopy(parts, 0, modifiedParts, 1, parts.length);
            modifiedParts[0] = host;
            parts = modifiedParts;
        }

        Source source = new Source(parts[0].replace(":", ""));
        String command = parts[1];
        ArrayList<String> params = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(parts, 2, parts.length)));

        if (verbose) {
            // log.info("Source: " + source.toString() + " command: " + command + " params: " + params);
        }

        ParserToken token = new ParserToken(line, source, command, params);
        for (Map.Entry<Tags, Parser> parser : parsers.entrySet()) {
            if (parser.getKey().toString().equals(command)) {
                Event e = parser.getValue().parse(ircManager, time, token);
                if (e != null) {
                    e.respond(new CommandEvent(ircManager, null, null, null, e, params, LogManager.getLogger(e.getClass().getSimpleName())));
                }
            }
        }
    }

    public abstract Event parse(IRCManager ircManager, long time, ParserToken token);
}
