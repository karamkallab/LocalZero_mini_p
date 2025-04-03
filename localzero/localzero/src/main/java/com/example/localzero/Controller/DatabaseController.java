package com.example.localzero.Controller;

public class DatabaseController {
    private Controller controller;

    public DatabaseController(Controller controller) {
        this.controller = controller;
    }

    public boolean registerUser(String name, String email, String password, String location, String userType) {
        // Logic to register the user in the database
        // This is a placeholder for actual database logic
        System.out.println("User registered: " + name + ", " + email + ", " + password + ", " + location + ", " + userType);
        return true; // Return true if registration is successful
        
    }
    
}

