package com.example.localzero.Controller;

import java.sql.*;
import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private Dotenv dotenv;

    public DatabaseConnection() {
        try {
            System.out.println("⏳ Laddar .env...");
            dotenv = Dotenv.configure()
        .directory(System.getProperty("user.dir"))
        .filename("localzero\\localzero\\.env")
        .load();

            String databasename = getDatabaseName();
            String user = getSQLUserName();
            String password = getSQLPassword();
            String url = "jdbc:postgresql://pgserver.mau.se:5432/" + databasename;

            System.out.println("📦 DATABASE_NAME: " + databasename);
        System.out.println("👤 USER_NAME: " + user);
        System.out.println("🔗 URL: " + url);

            connection = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Databasanslutning lyckades!");
        } catch (SQLException e) {
            System.out.println("❌ Fel vid anslutning:");
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

