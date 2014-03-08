/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
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
import tk.jomp16.sqlite_old.SQLiteManager;
import tk.jomp16.sqlite_old.configurator.SQLite_Configurator;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BotMain {
    private static Logger log = LogManager.getLogger(BotMain.class);
    private static SQLiteManager sqLiteManager;

    public static void main(String[] args) throws Exception {
        startIRCBot();
    }

    public static void startIRCBot() throws Exception {
        // TODO: CHANGE TO SQLITEOPENHELPER

        sqLiteManager = new SQLiteManager("database");

        try {
            sqLiteManager.getPreparedStatement("SELECT * FROM bot_config").close();
        } catch (SQLException e) {
            // If the sql fails, it is because the database is new
            new SQLite_Configurator();
        }

        LanguageManager languageManager = new LanguageManager("tk.jomp16.resource.Strings");

        log.trace(languageManager.getAsString("welcome", System.getProperty("user.name")));

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
