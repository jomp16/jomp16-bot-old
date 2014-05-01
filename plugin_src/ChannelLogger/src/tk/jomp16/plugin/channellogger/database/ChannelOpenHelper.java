/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.channellogger.database;

import tk.jomp16.sqlite.SQLiteDatabase;
import tk.jomp16.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

public class ChannelOpenHelper extends SQLiteOpenHelper {
    public ChannelOpenHelper(String databasePath, int databaseVersion) {
        super(databasePath, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase database) throws SQLException {
        database.executeFastUpdateQuery("CREATE TABLE IF NOT EXISTS logs " +
                "(channel string NOT NULL," +
                "timestamp string NOT NULL," +
                "action string NOT NULL," +
                "log string NOT NULL," +
                "user string NOT NULL)");
    }
}
