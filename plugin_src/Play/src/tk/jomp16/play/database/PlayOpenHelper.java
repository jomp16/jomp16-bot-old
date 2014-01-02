/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.play.database;

import tk.jomp16.sqlite1.SQLiteDatabase;
import tk.jomp16.sqlite1.SQLiteOpenHelper;

import java.sql.SQLException;

public class PlayOpenHelper extends SQLiteOpenHelper {
    public PlayOpenHelper(String databasePath, int databaseVersion) {
        super(databasePath, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase database) throws SQLException {
//        database.executeFastUpdateQuery("CREATE TABLE records (" +
//                "musicID integer NOT NULL, " +
//                "content string NOT NULL)");
//        database.executeFastUpdateQuery("CREATE TABLE musics (" +
//                "musicID integer NOT NULL, " +
//                "musicName string NOT NULL, " +
//                "author string NOT NULL)");
//        database.executeFastUpdateQuery("CREATE TABLE teste (testeblah string NOT NULL)");
//
//        database.insertData("teste", "a");
//        database.insertData("teste", "b");
//        database.insertData("teste", "c");
//        database.insertData("teste", "d");
//        database.insertData("teste", "e");
//
//        database.executeFastUpdateQuery("DROP TABLE teste");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) throws SQLException {
        database.dropTable("records");
    }
}
