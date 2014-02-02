/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.parser.parsers;

import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.Source;
import tk.jomp16.irc.channel.ChannelList;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.events.QuitEvent;
import tk.jomp16.irc.parser.Parser;
import tk.jomp16.irc.parser.ParserToken;
import tk.jomp16.irc.user.User;

public class QuitParser extends Parser {
    @Override
    public Event parse(IRCManager ircManager, ParserToken token) {
        // [DEBUG #QUIT]: :jomp16_!~jomp16@177.134.198.245 QUIT :Quit: Saindo
        // [DEBUG #QUIT]: [Quit: Saindo]
        // [DEBUG #QUIT]: Source{raw='jomp16_!~jomp16@177.134.198.245', nick='jomp16_', user='~jomp16', host='177.134.198.245'}

        User user = new User(token.getSource().getNick(), token.getSource().getUser(), token.getSource().getHost(), Source.loopMask(ircManager, token.getSource().getRaw()));

        ChannelList.removeUserFromAllChannel(user.getUserName());

        return new QuitEvent(ircManager, user, token.getParams().size() >= 1 ? token.getParams().get(0) : "");
    }
}
