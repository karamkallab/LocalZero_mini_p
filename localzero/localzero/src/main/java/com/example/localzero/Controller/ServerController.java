package com.example.localzero.Controller;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//TODO: Skapa en UserDTO klass, sedan gör du databas uppkopplingen.
@RestController
@CrossOrigin(origins = "*") // This annotation allows cross-origin requests from any origin.
@RequestMapping("/api")
public class ServerController {

    @PostMapping("/api/registration")
    public String authenticateUser(@RequestBody UserDTO userData) {
        // Skapa databas-kontroll
        DatabaseController dbController = new DatabaseController(null); // null för att vi inte använder Controller-klassen här
    
        // Anropa metoden som sparar användaren
        boolean success = dbController.registerUser(
            userData.name,
            userData.email,
            userData.password,
            userData.location,
            userData.role
        );

        // Svara till frontend
        if (success) {
            return "Registration successful!";
        } else {
            return "Something went wrong.";
        }
    }

    @PostMapping("/api/authenticator")
    public String loginUser(@RequestBody UserDTO userData) {
        
    }
}
