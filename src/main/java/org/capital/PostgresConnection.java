package org.capital;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {
    private static final String JDBC_URL = "jdbc:postgresql://127.0.0.1:5432/capital";
    private static final String USERNAME = "vgseven";
    private static final String PASSWORD = "seven";
5
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }
}
