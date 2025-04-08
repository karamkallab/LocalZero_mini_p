package com.example.localzero.Controller;

import java.sql.*;
import io.github.cdimascio.dotenv.Dotenv;


public class DatabaseController {
    private ServerController controller;
    private Connection conn;
    private DatabaseConnection dbConnection;

    public DatabaseController(ServerController controller) {
        this.controller = controller;
        this.dbConnection = DatabaseConnection.getInstance();
        this.conn = dbConnection.getConnection();
    }

    public boolean registerUser(String name, String email, String password, String location, String role) {
        CallableStatement stmt = null;
    
        try {
            stmt = conn.prepareCall("CALL register_user(?, ?, ?, ?, ?)");
    
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, location);
            stmt.setString(5, role);
    
            stmt.executeUpdate();
    
            System.out.println("User registered!");
            return true;
    
        } catch (Exception e) {
            e.printStackTrace();
            return false;
    
        } finally {
            try {
                if (stmt != null) stmt.close();  // ✅ Stäng bara statement
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
}

