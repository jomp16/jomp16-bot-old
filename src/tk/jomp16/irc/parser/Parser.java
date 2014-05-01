/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.Source;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.parser.parsers.*;
import tk.jomp16.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Parser {
    private static final Map<Tags, Parser> parsers = new HashMap<Tags, Parser>() {{
        put(Tags.COMMAND_ERROR, new ErrorParser());
        put(Tags.COMMAND_JOIN, new JoinParser());
        put(Tags.COMMAND_KICK, new KickParser());
        put(Tags.COMMAND_MODE, new ModeParser());
        put(Tags.COMMAND_NICK, new NickParser());
        put(Tags.COMMAND_PART, new PartParser());
        put(Tags.COMMAND_PING, new PingParser());
        put(Tags.COMMAND_PRIVMSG, new PrivMsgParser());
        put(Tags.COMMAND_QUIT, new QuitParser());
        put(Tags.COMMAND_TOPIC, new ChannelTopicParser(true));

        NickNameInUseParser nickNameInUseParser = new NickNameInUseParser();
        put(Tags.ERROR_NICK_IN_USE, nickNameInUseParser);
        put(Tags.ERROR_NICK_UNAVAILABLE, nickNameInUseParser);

        MotdParser motdParser = new MotdParser();
        put(Tags.RESPONSE_MOTD_CONTENT, motdParser);
        put(Tags.RESPONSE_MOTD_END, motdParser);
        put(Tags.RESPONSE_MOTD_START, motdParser);

        put(Tags.RESPONSE_NAMES_LIST, new NamesParser());

        put(Tags.RESPONSE_TOPIC_MESSAGE, new ChannelTopicParser(false));
    }};
    private static Logger log = LogManager.getLogger(Parser.class);
    private static String host = null;

    public static void parseLine(IRCManager ircManager, String rawLine) {
        if (rawLine == null) {
            throw new IllegalArgumentException("Can't process null lines");
        }

        List<String> parsedLine = Utils.tokenizeIRCLine(rawLine);

        log.debug(parsedLine);

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
            log.debug("Found parser for command: " + command);

            Parser parser = parsers.get(Tags.getTag(command));
            Event event = parser.parse(ircManager, token);

            if (event != null) {
                try {
                    event.respond();
                } catch (Exception e) {
                    log.error(e, e);
                }
            }
        } else {
            log.debug("Parser for command " + command + " not found");
        }
    }

    public static String getHost() {
        return host;
    }

    public abstract Event parse(IRCManager ircManager, ParserToken parserToken);
}
