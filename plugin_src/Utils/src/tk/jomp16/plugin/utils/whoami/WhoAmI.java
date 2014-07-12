/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.utils.whoami;

import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.event.CommandEvent;

public class WhoAmI extends Event {
    @Command("whoami")
    public void whoami(CommandEvent commandEvent) {
        commandEvent.respond("You're: " + commandEvent.getUser().getUserName() + ", and your level is: " + commandEvent.getUser().getLevel().toString().toLowerCase());
    }
}
