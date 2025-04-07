package com.example.localzero.Controller;

import java.sql.*;
import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            Dotenv dotenv = Dotenv.load();
            String username = dotenv.get("SQL_NAME");
            String password = dotenv.get("SQL_PASSWORD");

            String url = "jdbc:postgresql://pgserver.mau.se:5432/" + username;

            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Ansluten till databasen via .env!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
