/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.bot.database;

import tk.jomp16.sqlite.SQLiteDatabase;
import tk.jomp16.sqlite.SQLiteOpenHelper;
import tk.jomp16.ui.MainUI;
import tk.jomp16.ui.plugin.PluginUI;
import tk.jomp16.ui.sql.BotConfigurator;

import javax.swing.*;
import java.sql.SQLException;

public class BotOpenHelper extends SQLiteOpenHelper implements PluginUI {
    public static boolean wait = false;

    public BotOpenHelper(String databasePath, int databaseVersion) {
        super(databasePath, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase database) throws SQLException {
        database.executeFastUpdateQuery("CREATE TABLE bot_config (" +
                "nick string NOT NULL, " +
                "realName string NOT NULL, " +
                "password string NOT NULL, " +
                "identify string NOT NULL, " +
                "server string NOT NULL, " +
                "prefix string NOT NULL)");

        database.executeFastUpdateQuery("CREATE TABLE owners (mask string NOT NULL)");
        database.executeFastUpdateQuery("CREATE TABLE admins (mask string NOT NULL)");
        database.executeFastUpdateQuery("CREATE TABLE mods (mask string NOT NULL)");
        database.executeFastUpdateQuery("CREATE TABLE joinChannels (channel string NOT NULL)");

        if (MainUI.isGui()) {
            wait = true;

            new BotConfigurator(database);

            do {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (wait);
        } else {
            // todo
        }
    }

    @Override
    public JPanel getJPanel() {
        return null;
    }
}
