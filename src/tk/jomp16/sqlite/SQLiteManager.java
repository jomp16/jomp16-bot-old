/*
 * Copyright © 2013 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.sqlite;

import java.sql.*;

public class SQLiteManager {
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;

    public SQLiteManager(String databasePath) throws Exception {
        Class.forName("org.sqlite.JDBC");

        connection = DriverManager.getConnection(databasePath.endsWith(".db") ? "jdbc:sqlite:" + databasePath : "jdbc:sqlite:" + databasePath + ".db");

        statement = connection.createStatement();
    }

    public void executeFastUpdateQuery(String SQL, Object... params) throws SQLException {
        loopObject(SQL, params).executeUpdate();
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

    public void executeFastUpdateQuery(String SQL) throws SQLException {
        connection.prepareStatement(SQL).executeUpdate();
    }

    public ResultSet getResultSet(String SQL, Object... params) throws SQLException {
        return loopObject(SQL, params).executeQuery();
    }

    public void dropTable(String tableName) throws SQLException {
        connection.prepareStatement("DROP TABLE " + tableName).executeUpdate();
    }

    public void dropTableIfExists(String tableName) throws SQLException {
        connection.prepareStatement("DROP TABLE IF EXISTS " + tableName).executeUpdate();
    }

    public void insertData(String tableName, Object... data) throws SQLException {
        String tmpSQL = "INSERT INTO " + tableName + " VALUES (";

        for (Object ignored : data) {
            tmpSQL += "?, ";
        }

        tmpSQL = tmpSQL.substring(0, tmpSQL.length() - 2) + ")";

        loopObject(tmpSQL, data).executeUpdate();
    }

    public void deleteRow(String tableName, String columnName, int rowData) throws SQLException {
        connection.prepareStatement("DELETE FROM " + tableName + " WHERE " + columnName + "=" + rowData).executeUpdate();
    }

    public ResultSet getResultSet(String SQL) throws SQLException {
        return connection.prepareStatement(SQL).executeQuery();
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public PreparedStatement getPreparedStatement(String SQL) throws SQLException {
        return preparedStatement = connection.prepareStatement(SQL);
    }

    public void close() throws SQLException {
        statement.close();
        preparedStatement.close();
        connection.close();
    }
}