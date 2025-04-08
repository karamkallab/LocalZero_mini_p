package com.example.localzero.Controller;

import java.sql.*;
import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    public DatabaseConnection() {
        try {
            String user = getSQLUserName();
            String password = getSQLPassword();
            String url = "jdbc:postgresql://pgserver.mau.se:5432/" + user;

            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getSQLPassword(){
        Dotenv dotenv = Dotenv.configure()
                .directory(System.getProperty("user.dir"))
                .filename(".env")
                .load();

        String password = dotenv.get("SQL_PASSWORD");
        return password;
    }

    public String getSQLUserName(){
        Dotenv dotenv = Dotenv.configure()
                .directory(System.getProperty("user.dir"))
                .filename(".env")
                .load();

        String userNameEnvSQL = dotenv.get("SQL_NAME");
        return userNameEnvSQL;
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

