/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.event.events;

import com.jomp16.irc.event.Event;
import com.jomp16.irc.event.listener.CommandEvent;

public class PingEvent extends Event {
    @Override
    public void respond(CommandEvent commandEvent) {
        commandEvent.getIrcManager().getOutputRaw().writeRaw("PONG :" + commandEvent.getArgs().get(0));
    }
}
