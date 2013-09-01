/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.event.events;

import org.apache.logging.log4j.LogManager;
import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.user.User;

public class QuitEvent extends Event {
    private IRCManager ircManager;
    private User user;
    private String quitMessage;

    public QuitEvent(IRCManager ircManager, User user, String quitMessage) {
        this.ircManager = ircManager;
        this.user = user;
        this.quitMessage = quitMessage;
    }

    @Override
    public void respond() throws Exception {
        ircManager.getEvents().forEach((event) -> {
            try {
                event.onQuit(new tk.jomp16.irc.event.listener.event.QuitEvent(ircManager, user, quitMessage, LogManager.getLogger(event.getClass().getSimpleName())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
