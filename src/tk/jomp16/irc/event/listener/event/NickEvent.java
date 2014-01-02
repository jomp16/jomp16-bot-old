/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.event.listener.event;

import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.user.User;
import tk.jomp16.logger.Logger;

public class NickEvent {
    private IRCManager ircManager;
    private User user;
    private String oldNick;
    private String newNick;
    private Logger logger;

    public NickEvent(IRCManager ircManager, User user, String oldNick, String newNick, Logger logger) {
        this.ircManager = ircManager;
        this.user = user;
        this.oldNick = oldNick;
        this.newNick = newNick;
        this.logger = logger;
    }

    public IRCManager getIrcManager() {
        return ircManager;
    }

    public User getUser() {
        return user;
    }

    public String getOldNick() {
        return oldNick;
    }

    public String getNewNick() {
        return newNick;
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
