/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.programmer;

import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.CommandEvent;

public class NotABug extends Event {
    @Command("notabug")
    public void feature(CommandEvent commandEvent) {
        if (commandEvent.getArgs().size() >= 1) {
            commandEvent.respond(commandEvent.getArgs().get(0), "It's a feature, not a bug ™");
        }
    }
}
