/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.plugin.notes.database;

import tk.jomp16.sqlite.SQLiteDatabase;
import tk.jomp16.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

public class NotesOpenHelper extends SQLiteOpenHelper {
    public NotesOpenHelper(String databasePath, int databaseVersion) {
        super(databasePath, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase database) throws SQLException {
        database.executeFastUpdateQuery("CREATE TABLE IF NOT EXISTS notes " +
                "(user string NOT NULL, " +
                "title string NOT NULL, " +
                "message string NOT NULL, " +
                "date string NOT NULL, " +
                "time string NOT NULL)");
    }
}
