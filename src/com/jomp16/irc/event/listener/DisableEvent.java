/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.irc.event.listener;

import com.jomp16.irc.IRCManager;
import org.apache.logging.log4j.Logger;

public class DisableEvent extends InitEvent {
    public DisableEvent(IRCManager ircManager, Logger log) {
        super(ircManager, log);
    }
}
