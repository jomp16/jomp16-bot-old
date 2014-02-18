/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.sqlite;

import java.sql.SQLException;

public class SQLiteOpenHelper {
    private String databasePath;
    private int databaseVersion;

    public SQLiteOpenHelper(String databasePath, int databaseVersion) {
        this.databasePath = databasePath;
        this.databaseVersion = databaseVersion;
    }

    public void onCreate(SQLiteDatabase database) throws SQLException {

    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) throws SQLException {

    }

    public String getDatabasePath() {
        return databasePath;
    }

    public int getDatabaseVersion() {
        return databaseVersion;
    }

    public SQLiteDatabase getDatabase() throws Exception {
        return new SQLiteDatabase(this);
    }
}
