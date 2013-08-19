/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.event.events;

import com.jomp16.irc.IRCManager;
import com.jomp16.irc.event.Event;

public class PingEvent extends Event {
    private IRCManager ircManager;
    private String target;

    public PingEvent(IRCManager ircManager, String target) {
        this.ircManager = ircManager;
        this.target = target;
    }

    @Override
    public void respond() {
        ircManager.getOutputRaw().writeRaw("PONG :" + target);
    }
}
