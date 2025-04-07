package com.example.localzero.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DatabaseController {
    private Controller controller;
    private Connection conn;

    public DatabaseController(Controller controller) {
        this.controller = controller;
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    public boolean registerUser(String name, String email, String password, String location, String userType) {
        try {
            CallableStatement stmt = conn.prepareCall("CALL register_user(?, ?, ?, ?)");
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password); 
            stmt.setString(4, location);
    
            stmt.executeUpdate();
            stmt.close();
    
            System.out.println("The user is registred " + name);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
}

