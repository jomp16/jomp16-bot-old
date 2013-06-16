/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package com.jomp16.bot;

import com.jomp16.bot.event.GoogleEvent;
import com.jomp16.bot.event.PluginEvent;
import com.jomp16.bot.event.TestEvent;
import com.jomp16.irc.IRCManager;
import com.jomp16.irc.configuration.Configuration;
import com.jomp16.language.LanguageManager;
import com.jomp16.language.LanguageNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;

public class BotMain {
    private static IRCManager ircManager;
    private static Logger log = LogManager.getLogger(BotMain.class.getSimpleName());
    private static LanguageManager languageManager;
    private static String languageName;

    public static void main(String[] args) throws Exception {
        languageName = String.format("/lang/%s_%s.lang", System.getProperty("user.language"), System.getProperty("user.country"));
        boolean jar = BotMain.class.getResource("BotMain.class").toString().startsWith("jar:");

        try {
            languageManager = (jar) ? new LanguageManager(BotMain.class.getResourceAsStream(languageName)) : new LanguageManager(new FileInputStream(languageName.substring(1)));
        } catch (LanguageNotFoundException e) {
            languageName = String.format("/lang/%s_%s.lang", "en", "US");
            languageManager = (jar) ? new LanguageManager(BotMain.class.getResourceAsStream(languageName)) : new LanguageManager(new FileInputStream(languageName.substring(1)));
        }

        log.trace(languageManager.getString("Welcome"));

        ircManager = new IRCManager(new Configuration.Builder()
                .setPrefix("*")
                .setVerbose(true)
                .buildConfiguration());

        ircManager.registerEvent(new TestEvent());
        ircManager.registerEvent(new GoogleEvent());
        ircManager.registerEvent(new PluginEvent());

        ircManager.addAdmin("~jomp16@unaffiliated/jomp16");

        ircManager.connect1();
        ircManager.getOutputIRC().joinChannel("#jomp16-bot");
    }
}
