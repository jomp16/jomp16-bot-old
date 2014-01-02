/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.event.events;

import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.channel.Channel;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.user.User;
import tk.jomp16.logger.LogManager;

public class PartEvent extends Event {
    private IRCManager ircManager;
    private User user;
    private Channel channel;
    private String reason;

    public PartEvent(IRCManager ircManager, User user, Channel channel, String reason) {
        this.ircManager = ircManager;
        this.user = user;
        this.channel = channel;
        this.reason = reason;
    }

    @Override
    public void respond() throws Exception {
        ircManager.getEvents().forEach((event) -> {
            try {
                event.onPart(new tk.jomp16.irc.event.listener.event.PartEvent(ircManager, user, channel, reason, LogManager.getLogger(event.getClass().getSimpleName())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
