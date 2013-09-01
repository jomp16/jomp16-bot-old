/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.irc.plugin.about;

import tk.jomp16.irc.event.CommandFilter;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.CommandEvent;
import tk.jomp16.irc.event.listener.InitEvent;
import tk.jomp16.irc.plugin.help.HelpRegister;

public class About extends Event {
    @CommandFilter("about")
    public void about(CommandEvent commandEvent) {
        commandEvent.respond("jomp16-bot written by jomp16, with help (code and tips) from nebkat, licensed under WTFPL, source code at https://github.com/jomp16/jomp16-bot");
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        initEvent.addHelp(this, new HelpRegister("about", "Return the info about this bot", ""));
    }
}
