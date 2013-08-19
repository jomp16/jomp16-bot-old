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
import com.jomp16.irc.parser.parsers.*;
import com.jomp16.irc.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Parser {
    private static final HashMap<Tags, Parser> parsers = new HashMap<Tags, Parser>() {{
        put(Tags.RESPONSE_TOPIC_MESSAGE, new ChannelTopicParser());
        put(Tags.COMMAND_JOIN, new JoinParser());
        put(Tags.COMMAND_KICK, new KickParser());
        put(Tags.COMMAND_MODE, new ModeParser());
        put(Tags.RESPONSE_NAMES_LIST, new NamesParser());
        put(Tags.ERROR_NICK_IN_USE, new NickNameInUseParser());
        put(Tags.COMMAND_NICK, new NickParser());
        put(Tags.COMMAND_PART, new PartParser());
        put(Tags.COMMAND_PING, new PingParser());
        put(Tags.COMMAND_PRIVMSG, new PrivMsgParser());
        put(Tags.COMMAND_QUIT, new QuitParser());
    }};
    private static Logger log = LogManager.getLogger(Parser.class.getSimpleName());
    private static String host = null;

    public static void parseLine(IRCManager ircManager, String rawLine) {
        new Thread(() -> {
            if (rawLine == null) {
                throw new IllegalArgumentException("Can't process null lines");
            }

            ArrayList<String> parsedLine = Utils.tokenizeLine(rawLine);

            log.trace(parsedLine);

            if (host == null) {
                host = parsedLine.get(0);
            }

            if (!parsedLine.get(0).startsWith(":")) {
                parsedLine.add(0, host);
            }

            Source source = new Source(parsedLine.remove(0).replace(":", ""));
            String command = parsedLine.remove(0).toUpperCase();

            ParserToken token = new ParserToken(rawLine, source, command, parsedLine);

            if (parsers.containsKey(Tags.getTag(command))) {
                log.info("Found parser for command: " + command);

                new Thread(() -> {
                    Event event = parsers.get(Tags.getTag(command)).parse(ircManager, token);

                    if (event != null) {
                        try {
                            event.respond();
                        } catch (Exception e) {
                            log.error(e);
                        }
                    }
                }).start();
            } else {
                log.info("Parser for command " + command + " not found");
            }
        }).start();
    }

    public abstract Event parse(IRCManager ircManager, ParserToken token);
}
