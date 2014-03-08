/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.bot.database;

import tk.jomp16.sqlite.SQLiteDatabase;
import tk.jomp16.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

public class BotOpenHelper extends SQLiteOpenHelper {
    public BotOpenHelper(String databasePath, int databaseVersion) {
        super(databasePath, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase database) throws SQLException {
        database.executeFastUpdateQuery("CREATE TABLE bot_config (" +
                "nick string NOT NULL, " +
                "realName string NOT NULL, " +
                "identify string NOT NULL, " +
                "prefix string NOT NULL, " +
                "server string NOT NULL)");

        database.executeFastUpdateQuery("CREATE TABLE owners (mask string NOT NULL)");
        database.executeFastUpdateQuery("CREATE TABLE admins (mask string NOT NULL)");
        database.executeFastUpdateQuery("CREATE TABLE mods (mask string NOT NULL)");
        database.executeFastUpdateQuery("CREATE TABLE joinChannel (channel string NOT NULL)");
    }
}
