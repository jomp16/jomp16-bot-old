/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.event.listener.event;

import org.apache.logging.log4j.Logger;
import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.event.listener.Listener;
import tk.jomp16.irc.parser.parsers.CtcpParser;
import tk.jomp16.irc.user.User;

public class CtcpEvent extends Listener {
    private IRCManager ircManager;
    private User user;
    private CtcpParser.CtcpCommands ctcpCommands;
    private String raw;
    private Logger logger;

    public CtcpEvent(IRCManager ircManager, User user, CtcpParser.CtcpCommands ctcpCommands, String raw, Logger logger) {
        super(ircManager, logger);

        this.ircManager = ircManager;
        this.user = user;
        this.ctcpCommands = ctcpCommands;
        this.raw = raw;
        this.logger = logger;
    }
}
