/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.event.listener;

import com.jomp16.irc.IRCManager;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.plugin.help.HelpRegister;
import org.apache.logging.log4j.Logger;

import java.net.URLDecoder;

public class InitEvent {
    private IRCManager ircManager;
    private Logger log;

    public InitEvent(IRCManager ircManager, Logger log) {
        this.ircManager = ircManager;
        this.log = log;
    }

    public IRCManager getIrcManager() {
        return ircManager;
    }

    public Logger getLog() {
        return log;
    }

    public void addHelp(Event event, HelpRegister helpRegister) {
        event.registerHelp(helpRegister);
    }

    public String getPluginPath(Event event) {
        try {
            return URLDecoder.decode(event.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }
}
