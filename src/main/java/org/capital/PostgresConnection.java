package org.capital;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {
    private static final String JDBC_URL = "jdbc:postgresql://127.0.0.1:5432/capital";
    private static final String USERNAME = "vgseven";
    private static final String PASSWORD = "seven";

    // neon-database connection variables

//    private static final String JDBC_URL = "jdbc:postgresql://ep-broad-mud-a5uoykp4-pooler.us-east-2.aws.neon.tech/capital";
//    private static final String USERNAME = "vgseven";
//    private static final String PASSWORD = "XMr9WQliRV6w";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }
}
