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
import tk.jomp16.irc.channel.ChannelList;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.events.ModeEvent;
import tk.jomp16.irc.parser.Parser;
import tk.jomp16.irc.parser.ParserToken;
import tk.jomp16.irc.user.User;

public class ModeParser extends Parser {
    @Override
    public Event parse(IRCManager ircManager, ParserToken token) {
        if (token.getSource().getRaw().equals(ircManager.getConfiguration().getNick())) {
            return null;
        }

        // TODO: ADD AND REMOVE BANNEDS USERS

        //System.out.println("[DEBUG #MODE]: " + token.getRawLine());
        //System.out.println("[DEBUG #MODE]: " + token.getParams());
        //System.out.println("[DEBUG #MODE]: " + token.getSource());

        // [DEBUG #MODE]: :jomp16!~jomp16@unaffiliated/jomp16 MODE #jomp16-bot +v jomp16_
        // [DEBUG #MODE]: [#jomp16-bot, +v, jomp16_]
        // [DEBUG #MODE]: Source{raw='jomp16!~jomp16@unaffiliated/jomp16', nick='jomp16', user='~jomp16', host='unaffiliated/jomp16'}

        User user = new User(token.getSource().getNick(), token.getSource().getUser(), token.getSource().getHost(), Source.loopMask(ircManager, token.getSource().getRaw()));

        Channel channel = new Channel(token.getParams().get(0));
        ModeEvent.Modes mode = ModeEvent.Modes.getMode(token.getParams().get(1));
        String user1 = token.getParams().get(2);

        ChannelList.changeUserLevel(channel.getTargetName(), user1, mode);

        return new ModeEvent(ircManager, user, channel, user1, mode);
    }
}
