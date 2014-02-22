/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.about;

import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.event.CommandEvent;
import tk.jomp16.irc.event.listener.event.InitEvent;
import tk.jomp16.language.LanguageManager;
import tk.jomp16.plugin.help.HelpRegister;

public class About extends Event {
    private LanguageManager languageManager;

    @Command("about")
    public void about(CommandEvent commandEvent) {
        commandEvent.respond(languageManager.getAsString("about.text.about"));
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        languageManager = new LanguageManager("tk.jomp16.plugin.resource.Strings");

        initEvent.addHelp(this, new HelpRegister("about", languageManager.getAsString("about.help.text")));
    }
}
