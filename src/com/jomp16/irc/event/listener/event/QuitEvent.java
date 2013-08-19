/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.event.listener.event;

import com.jomp16.irc.IRCManager;
import com.jomp16.irc.user.User;
import org.apache.logging.log4j.Logger;

public class QuitEvent {
    private IRCManager ircManager;
    private User user;
    private String reason;
    private Logger logger;

    public QuitEvent(IRCManager ircManager, User user, String reason, Logger logger) {
        this.ircManager = ircManager;
        this.user = user;
        this.reason = reason;
        this.logger = logger;
    }

    public IRCManager getIrcManager() {
        return ircManager;
    }

    public User getUser() {
        return user;
    }

    public String getReason() {
        return reason;
    }

    public Logger getLogger() {
        return logger;
    }

    public synchronized void respond(Object target, Object message) {
        ircManager.getOutputIRC().sendMessage(target, message);

        try {
            wait(700);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public synchronized void respond(Object target, Object user, Object message) {
        ircManager.getOutputIRC().sendMessage(target, user, message);

        try {
            wait(700);
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
