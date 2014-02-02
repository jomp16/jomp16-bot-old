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
import tk.jomp16.irc.event.events.PrivMsgEvent;
import tk.jomp16.irc.parser.Parser;
import tk.jomp16.irc.parser.ParserToken;
import tk.jomp16.irc.user.User;

public class PrivMsgParser extends Parser {
    @Override
    public Event parse(IRCManager ircManager, ParserToken token) {
        if (token.getSource().getNick().equals(ircManager.getConfiguration().getNick())) {
            return null;
        }

        User user = new User(token.getSource().getNick(), token.getSource().getUser(), token.getSource().getHost(), Source.loopMask(ircManager, token.getSource().getRaw()));

        Channel channel;

        if (token.getParams().get(0).startsWith("#")) {
            channel = new Channel(token.getParams().remove(0));
        } else {
            channel = new Channel(user.getUserName());
            token.getParams().remove(0);
        }

        String message = token.getParams().get(0);

        PrivMsgEvent.PrivMSGTag tag = PrivMsgEvent.PrivMSGTag.NORMAL;

        if (message.startsWith("\u0001")) {
            message = message.replace("\u0001", "");

            if (message.startsWith("PING ")) {
                tag = PrivMsgEvent.PrivMSGTag.PING;
            } else if (message.startsWith("ACTION ")) {
                tag = PrivMsgEvent.PrivMSGTag.ACTION;
            }

            message = message.substring(message.indexOf(' ') + 1);
        }

        new PrivMsgEvent(ircManager, user, channel, message, tag);
        return null;
    }
}
