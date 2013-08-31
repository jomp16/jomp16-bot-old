/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.event.events;

import com.jomp16.irc.IRCManager;
import com.jomp16.irc.event.Event;

public class NickNameInUseEvent extends Event {
    private static int num = 1;
    private IRCManager ircManager;

    public NickNameInUseEvent(IRCManager ircManager) {
        this.ircManager = ircManager;
    }

    @Override
    public void respond() throws Exception {
        ircManager.getOutputIRC().changeNick(ircManager.getConfiguration().getNick() + "_" + num);
        num++;
    }
}
