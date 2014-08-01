/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.parser.parsers;

import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.Source;
import tk.jomp16.irc.channel.Channel;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.events.CtcpEvent;
import tk.jomp16.irc.parser.Parser;
import tk.jomp16.irc.parser.ParserToken;
import tk.jomp16.irc.user.User;

public class CtcpParser extends Parser {
    @Override
    public Event parse(IRCManager ircManager, ParserToken token) {
        System.out.println("----------------");
        System.out.println("CTCP");
        System.out.println(token.getCommand());
        System.out.println(token.getParams());
        System.out.println(token.getRawLine());
        System.out.println(token.getSource());
//        System.out.println(token.getParams().get(0).substring(1, 4));
        CtcpCommands ctcpCommands = CtcpCommands.getCtcpCommandFromString(token.getParams().get(0).split(" ")[0]);

        System.out.println(ctcpCommands);
        System.out.println("----------------");

        User user = new User(token.getSource().getNick(), token.getSource().getUser(), token.getSource().getHost(), Source.loopMask(ircManager, token.getSource().getRaw()));
        Channel channel;

        if (token.getParams().get(0).startsWith("#")) {
            channel = new Channel(token.getParams().remove(0));
        } else {
            channel = new Channel(user.getUserName());
        }

        return new CtcpEvent(ircManager, user, channel, ctcpCommands, token.getParams().get(0));
    }

    public enum CtcpCommands {
        TIME,
        DCC,
        PING;

        public static CtcpCommands getCtcpCommandFromString(String command) {
            if (command.startsWith("\u0001")) {
                command = command.replace("\u0001", "");

                return CtcpCommands.valueOf(command);
            } else {
                return null;
            }
        }
    }
}
