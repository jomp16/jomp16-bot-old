/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.imdb;

import tk.jomp16.irc.event.Command;
import tk.jomp16.irc.event.Event;
import tk.jomp16.irc.event.listener.event.CommandEvent;
import tk.jomp16.irc.event.listener.event.InitEvent;
import tk.jomp16.language.LanguageManager;

public class IMDb extends Event {
    private LanguageManager languageManager;

    @Command(value = "imdb", args = {"movie:", "year:", "tomatoes"})
    public void imdb(CommandEvent commandEvent) {
        /*if (commandEvent.getOptionSet().has("movie") && commandEvent.getOptionSet().has("year")) {
            commandEvent.respond(commandEvent.getOptionSet().valueOf("movie") + " " + commandEvent.getOptionSet().valueOf("year"));
        }

        if (commandEvent.getOptionSet().has("tomatoes")) {
            commandEvent.respond("TOMATO FOR ME AND YOU!");
        }*/
    }

    @Override
    public void onInit(InitEvent initEvent) throws Exception {
        languageManager = initEvent.getLanguageManager(this, "tk.jomp16.plugin.imdb.resource.Strings");
    }
}
