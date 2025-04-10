package com.example.localzero.Controller;

import java.sql.*;

import org.springframework.stereotype.Component;

import io.github.cdimascio.dotenv.Dotenv;

@Component
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private Dotenv dotenv;

    public DatabaseConnection() {
        try {
            dotenv = Dotenv.configure()
        .directory(System.getProperty("user.dir"))
        .filename("localzero\\localzero\\.env")
        .load();

            String databasename = getDatabaseName();
            String user = getSQLUserName();
            String password = getSQLPassword();
            String url = "jdbc:postgresql://pgserver.mau.se:5432/" + databasename;


            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connection successful!");
        } catch (SQLException e) {
            System.out.println("Error during database connection:");
            e.printStackTrace();
        }
    }

    public String getSQLPassword(){
        String password = dotenv.get("SQL_PASSWORD");
        return password;
    }

    public String getDatabaseName() {
        String userNameEnvSQL = dotenv.get("DATABASE_NAME");
        return userNameEnvSQL;
    }

    public String getSQLUserName() {
        String userNameEnvSQL = dotenv.get("USER_NAME");
        return userNameEnvSQL;
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        if(connection != null){
            return connection;
        }

        return null;
    }
}

