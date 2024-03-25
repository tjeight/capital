package org.capital;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {
    private static final String JDBC_URL = "jdbc:postgresql://ep-proud-cherry-a5lm4m6t-pooler.us-east-2.aws.neon.tech:5432/capital";
    private static final String USERNAME = "vgseven";
    private static final String PASSWORD = "t32vCApeVTci";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }
}