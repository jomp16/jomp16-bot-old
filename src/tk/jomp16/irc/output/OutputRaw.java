/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.output;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tk.jomp16.irc.IRCManager;

import java.io.IOException;

public class OutputRaw {
    private IRCManager ircManager;
    private Logger log = LogManager.getLogger(this.getClass());

    public OutputRaw(IRCManager ircManager) {
        this.ircManager = ircManager;
    }

    public void writeRaw(String line) {
        log.debug(line);

        try {
            ircManager.getIrcWriter().write(line);
            ircManager.getIrcWriter().newLine();
            ircManager.getIrcWriter().flush();
        } catch (IOException e) {
            log.error(e, e);
        }
    }
}
