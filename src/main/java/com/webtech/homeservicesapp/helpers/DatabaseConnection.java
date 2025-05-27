package com.webtech.homeservicesapp.helpers;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/DomuSwap_db";
    private static final String USER = "postgres";  // Your PostgreSQL username
    private static final String PASSWORD = "postgres";  // Your PostgreSQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
