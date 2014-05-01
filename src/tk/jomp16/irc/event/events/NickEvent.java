/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.event.events;

import org.apache.logging.log4j.LogManager;
import tk.jomp16.irc.IRCManager;
import tk.jomp16.irc.channel.Channel;
import tk.jomp16.irc.channel.ChannelDAO;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.user.User;

public class NickEvent extends Event {
    private IRCManager ircManager;
    private User user;
    private Channel channel;
    private String oldNick;
    private String newNick;

    public NickEvent(IRCManager ircManager, User user, Channel channel, String oldNick, String newNick) {
        this.ircManager = ircManager;
        this.user = user;
        this.channel = channel;
        this.oldNick = oldNick;
        this.newNick = newNick;
    }

    @Override
    public void respond() throws Exception {
        Runnable runnable = () -> ircManager.getEvents().forEach((event) -> {
            try {
                tk.jomp16.irc.event.listener.event.NickEvent nickEvent =
                        new tk.jomp16.irc.event.listener.event.NickEvent(ircManager, user, channel,
                                new ChannelDAO(ircManager, channel), LogManager.getLogger(event.getClass()));
                nickEvent.setOldNick(oldNick);
                nickEvent.setNewNick(newNick);

                event.onNick(nickEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        ircManager.getExecutor().execute(runnable);
    }
}
