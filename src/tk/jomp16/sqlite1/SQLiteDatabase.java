/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.sqlite1;

import java.io.File;
import java.sql.*;

public class SQLiteDatabase {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private boolean newDatabase = true;
    private int databaseVersion = 1;

    public SQLiteDatabase(SQLiteOpenHelper openHelper) throws Exception {
        this.databaseVersion = openHelper.getDatabaseVersion();

        createDatabase(openHelper.getDatabasePath());

        int storedDatabaseVersion = getResultSet("SELECT * FROM private_info").getInt("databaseVersion");

        if (newDatabase) {
            openHelper.onCreate(this);
        } else if (databaseVersion > storedDatabaseVersion) {
            executeFastUpdateQuery("UPDATE private_info SET databaseVersion = ?", databaseVersion);
            openHelper.onUpgrade(this, storedDatabaseVersion, databaseVersion);
        }
    }

    private void createDatabase(String databasePath) throws Exception {
        Class.forName("org.sqlite.JDBC");

        File f = new File(databasePath);

        if (f.exists()) {
            newDatabase = false;
        }

        connection = DriverManager.getConnection(databasePath.endsWith(".db") ? "jdbc:sqlite:" + databasePath : "jdbc:sqlite:" + databasePath + ".db");

        if (newDatabase) {
            executeFastUpdateQuery("CREATE TABLE private_info (databaseVersion integer NOT NULL)");
            insertData("private_info", databaseVersion);
        }
    }

    public int executeFastUpdateQuery(String SQL, Object... params) throws SQLException {
        return loopObject(SQL, params).executeUpdate();
    }

    private PreparedStatement loopObject(String SQL, Object... params) throws SQLException {
        int tmp = 1;

        preparedStatement = connection.prepareStatement(SQL);

        for (Object object : params) {
            preparedStatement.setObject(tmp, object);

            tmp++;
        }

        return preparedStatement;
    }

    public int executeFastUpdateQuery(String SQL) throws SQLException {
        return connection.prepareStatement(SQL).executeUpdate();
    }

    public ResultSet getResultSet(String SQL, Object... params) throws SQLException {
        return loopObject(SQL, params).executeQuery();
    }

    public int dropTable(String tableName) throws SQLException {
        return executeFastUpdateQuery("DROP TABLE " + tableName);
    }

    public int dropTableIfExists(String tableName) throws SQLException {
        return executeFastUpdateQuery("DROP TABLE IF EXISTS " + tableName);
    }

    public int insertData(String tableName, Object... data) throws SQLException {
        String tmpSQL = "INSERT INTO " + tableName + " VALUES (";

        for (Object ignored : data) {
            tmpSQL += "?, ";
        }

        tmpSQL = tmpSQL.substring(0, tmpSQL.length() - 2) + ")";

        return loopObject(tmpSQL, data).executeUpdate();
    }

//    private void insertOrReplaceData(String tableName, Object... data) throws SQLException {
//        String tmpSQL = "INSERT OR REPLACE INTO " + tableName + " VALUES (";
//
//        for (Object ignored : data) {
//            tmpSQL += "?, ";
//        }
//
//        tmpSQL = tmpSQL.substring(0, tmpSQL.length() - 2) + ")";
//
//        loopObject(tmpSQL, data).executeUpdate();
//    }

    public int deleteRow(String tableName, String columnName, int rowData) throws SQLException {
        return executeFastUpdateQuery("DELETE FROM " + tableName + " WHERE " + columnName + " = ?", rowData);
    }

    public void close() throws SQLException {
        preparedStatement.close();
        connection.close();
    }
}
