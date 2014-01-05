/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.bot.database;

import tk.jomp16.sqlite1.SQLiteDatabase;
import tk.jomp16.sqlite1.SQLiteOpenHelper;

import java.sql.SQLException;

public class BotOpenHelper extends SQLiteOpenHelper {
    public BotOpenHelper(String databasePath, int databaseVersion) {
        super(databasePath, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase database) throws SQLException {

    }
}