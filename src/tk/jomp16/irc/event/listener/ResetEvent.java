/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.event.listener;

import tk.jomp16.irc.IRCManager;
import tk.jomp16.logger.Logger;

public class ResetEvent extends InitEvent {
    public ResetEvent(IRCManager ircManager, Logger log) {
        super(ircManager, log);
    }
}
