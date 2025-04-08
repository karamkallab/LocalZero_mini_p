package com.example.localzero.Controller;

import java.sql.*;
import io.github.cdimascio.dotenv.Dotenv;


public class DatabaseController {
    private Controller controller;
    private Connection conn;

    public DatabaseController(Controller controller) {
        this.controller = controller;
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    public boolean registerUser(String name, String email, String password, String location, String role) {
        String URL = getSQLURL();
        String SQLpassword = getSQLPassword();
        String userNameEnvSQL = getSQLUserName();
        try {
            Connection con = DatabaseConnection.getInstance().getConnection();
            CallableStatement stmt = con.prepareCall("CALL register_user(?, ?, ?, ?, ?)");
    
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, location);
            stmt.setString(5, role);
    
            stmt.executeUpdate();
            stmt.close();
    
            System.out.println("User registered!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public String getSQLURL(){
        Dotenv dotenv = Dotenv.configure()
                .directory(System.getProperty("user.dir"))
                .filename(".env")
                .load();

        String URL = dotenv.get("SQL_URL");
        return URL;
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
    
}

