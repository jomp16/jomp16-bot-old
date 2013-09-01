/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
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
import tk.jomp16.irc.event.events.KickEvent;
import tk.jomp16.irc.parser.Parser;
import tk.jomp16.irc.parser.ParserToken;
import tk.jomp16.irc.user.User;

public class KickParser extends Parser {
    @Override
    public Event parse(IRCManager ircManager, ParserToken token) {
        // [DEBUG #KICK]: :jomp16!~jomp16@unaffiliated/jomp16 KICK #jomp16-bot Willy_Wonka :Willy_Wonka
        // [DEBUG #KICK]: [#jomp16-bot, Willy_Wonka, Willy_Wonka]
        // [DEBUG #KICK]: Source{raw='jomp16!~jomp16@unaffiliated/jomp16', nick='jomp16', user='~jomp16', host='unaffiliated/jomp16'}

        User user = new User(token.getSource().getNick(), token.getSource().getUser(), token.getSource().getHost(), Source.loopMask(ircManager, token.getSource().getRaw()));
        Channel channel = new Channel(token.getParams().get(0));

        String user1 = token.getParams().get(1);
        String reason = token.getParams().get(2);

        ChannelList.removeUserToChannel(channel.getTargetName(), user1);

        return new KickEvent(ircManager, user, channel, user1, reason);
    }
}
