// Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
// This work is free. You can redistribute it and/or modify it under the
// terms of the Do What The Fuck You Want To Public License, Version 2,
// as published by Sam Hocevar. See the COPYING file for more details.

package tk.jomp16.plugin.play.database;

import tk.jomp16.sqlite1.SQLiteDatabase;
import tk.jomp16.sqlite1.SQLiteOpenHelper;

import java.sql.SQLException;

public class PlayOpenHelper extends SQLiteOpenHelper {
    public PlayOpenHelper(String databasePath, int databaseVersion) {
        super(databasePath, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase database) throws SQLException {
        database.executeFastUpdateQuery("CREATE TABLE records (" +
                "musicID INTEGER NOT NULL, " +
                "content STRING NOT NULL)");
        database.executeFastUpdateQuery("CREATE TABLE musics (" +
                "musicID INTEGER PRIMARY KEY, " +
                "musicName STRING NOT NULL, " +
                "author STRING NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) throws SQLException {
        database.dropTable("records");
    }
}
