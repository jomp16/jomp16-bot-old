/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tk.jomp16.bot.plugin.FunCommandsPlugin;
import tk.jomp16.bot.plugin.TestPlugin;
import tk.jomp16.configuration.Configuration;
import tk.jomp16.irc.IRCManager;
import tk.jomp16.language.LanguageManager;
import tk.jomp16.language.LanguageNotFoundException;
import tk.jomp16.sqlite.SQLiteManager;
import tk.jomp16.sqlite.configurator.SQLite_Configurator;

import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BotMain {
    private static Logger log = LogManager.getLogger(BotMain.class.getSimpleName());
    private static SQLiteManager sqLiteManager;

    public static void main(String[] args) throws Exception {
        startIRCBot();
    }

    private static void startIRCBot() throws Exception {
        sqLiteManager = new SQLiteManager("database");

        try {
            sqLiteManager.getPreparedStatement("SELECT * FROM bot_config").close();
        } catch (SQLException e) {
            // If the sql fails, it is because the database is new
            new SQLite_Configurator();
        }

        String languageName = String.format("/lang/%s_%s.lang", System.getProperty("user.language"), System.getProperty("user.country"));
        boolean jar = BotMain.class.getResource("BotMain.class").toString().startsWith("jar:");

        LanguageManager languageManager;

        try {
            languageManager = new LanguageManager(jar ? BotMain.class.getResourceAsStream(languageName) : new FileInputStream(languageName.substring(1)));
        } catch (LanguageNotFoundException e) {
            languageName = String.format("/lang/%s_%s.lang", "en", "US");
            languageManager = new LanguageManager(jar ? BotMain.class.getResourceAsStream(languageName) : new FileInputStream(languageName.substring(1)));
        }

        log.trace(languageManager.getString("Welcome"));

        ResultSet ircConf = sqLiteManager.getResultSet("SELECT * FROM bot_config");

        IRCManager ircManager = new IRCManager(new Configuration.Builder()
                .setNick(ircConf.getString("nick"))
                .setRealName(ircConf.getString("realName"))
                .setPassword(ircConf.getString("password"))
                .setPrefix(ircConf.getString("prefix"))
                .setIdentify(ircConf.getString("identify"))
                .setServer(ircConf.getString("server"))
                .setVerbose(true)
                .buildConfiguration());

        ircManager.registerEvent(new TestPlugin(), true);
        ircManager.registerEvent(new FunCommandsPlugin(), true);

        ResultSet resultSet = sqLiteManager.getResultSet("SELECT * FROM owners");
        while (resultSet.next()) {
            ircManager.addOwner(resultSet.getString("mask"));
        }

        ircManager.connect();
        ircManager.getOutputIRC().joinChannel("#jomp16-bot");
    }
}