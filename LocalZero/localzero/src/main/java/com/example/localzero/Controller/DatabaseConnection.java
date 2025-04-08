package com.example.localzero.Controller;

import java.sql.*;
import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            Dotenv dotenv = Dotenv.load();
            String user = dotenv.get("SQL_NAME");
            String password = dotenv.get("SQL_PASSWORD");
            String url = "jdbc:postgresql://pgserver.mau.se:5432/" + user;

            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}

