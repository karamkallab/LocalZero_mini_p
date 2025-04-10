package com.example.localzero.Controller;

import java.util.HashMap;
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
    private DatabaseController dbController = new DatabaseController(this);

    @PostMapping("/registration")
    public String authenticateUser(@RequestBody UserDTO userData) {            
        boolean success = dbController.registerUser(
            userData.getName(),
            userData.getEmail(),
            userData.getPassword(),
            userData.getLocation(),
            userData.getRole()
        );
        
        if (success) {
            return "success";
        } else {
            return "Something went wrong.";
        }
    }

    @PostMapping("/authenticator")
    public String loginUser(@RequestBody HashMap<String, String> user) {
        String email = user.get("user_email");
        String password = user.get("user_password");

        boolean success = dbController.checkIfUserExistsBeforeLogIn(
            email, password);

        if(success) {
            return "true";
        } else {
            return "Login failed!";
        }
    }

    @PostMapping("/initiative")
    public String createInitiative(@RequestBody InitiativeDTO data) {
    boolean success = dbController.createInitiative(
        data.getTitle(),
        data.getDescription(),
        data.getLocation(),
        data.getCategory(),
        data.getVisibility()
    );

    if (success) {
        return "Initiative created!";
    } else {
        return "Something went wrong.";
    }
}

}
