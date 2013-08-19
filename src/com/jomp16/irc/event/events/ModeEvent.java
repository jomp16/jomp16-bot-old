/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.event.events;

import com.jomp16.irc.IRCManager;
import com.jomp16.irc.channel.Channel;
import com.jomp16.irc.event.Event;
import com.jomp16.irc.user.User;
import org.apache.logging.log4j.LogManager;

public class ModeEvent extends Event {
    private IRCManager ircManager;
    private User user;
    private Channel channel;
    private String userModed;
    private Modes mode;

    public ModeEvent(IRCManager ircManager, User user, Channel channel, String userModed, Modes mode) {
        this.ircManager = ircManager;
        this.user = user;
        this.channel = channel;
        this.userModed = userModed;
        this.mode = mode;
    }

    @Override
    public void respond() {
        for (Event event : ircManager.getEvents()) {
            event.onMode(new com.jomp16.irc.event.listener.event.ModeEvent(ircManager, user, channel, userModed, mode, LogManager.getLogger(event.getClass().getSimpleName())));
        }
    }

    public static enum Modes {
        NORMAL(""),
        VOICE("+v"),
        OP("+o"),
        DEVOICE("-v"),
        DEOP("-o");
        private String mode;

        private Modes(String s) {
            this.mode = s;
        }

        public static Modes getMode(String mode) {
            for (Modes modes : values()) {
                if (modes.mode.equals(mode)) {
                    return modes;
                }
            }

            return NORMAL;
        }
    }
}
