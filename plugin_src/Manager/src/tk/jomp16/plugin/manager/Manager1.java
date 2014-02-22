/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.manager;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.Level;
import tk.jomp16.irc.event.listener.event.CommandEvent;

public class Manager1 extends Event {
    @Command(value = "manager", level = Level.OWNER)
    public void manager(CommandEvent commandEvent) {
        OptionParser optionParser = new OptionParser();
        optionParser.allowsUnrecognizedOptions();

        optionParser.accepts("level");
        optionParser.accepts("add").requiredIf("level").withRequiredArg();
        optionParser.accepts("remove").requiredIf("level").withRequiredArg();

        OptionSet optionSet = optionParser.parse(commandEvent.getArgs());

        if (optionSet.has("level")) {
            if (optionSet.has("add")) {
                commandEvent.respond("add");
            }

            if (optionSet.has("remove")) {
                commandEvent.respond("remove");
            }

            commandEvent.respond("WAT!");
        } else {
            commandEvent.respond("WAT?!");
        }
    }
}
