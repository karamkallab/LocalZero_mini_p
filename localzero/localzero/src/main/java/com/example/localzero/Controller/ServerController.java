package com.example.localzero.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.localzero.DTO.InitiativeDTO;
import com.example.localzero.DTO.UserDTO;

// @RestController makes this class a web controller that handles HTTP requests and returns responses as JSON.
// @CrossOrigin allows cross-origin requests from any origin, which is useful for development when the frontend and backend are on different servers.
// @RequestMapping specifies the base URL for all endpoints in this controller, in this case, "/api".
@RestController
@CrossOrigin(origins = "*") // This annotation allows cross-origin requests from any origin.
@RequestMapping("/api")
public class ServerController {
    private final UserService userService;

    @Autowired
    public ServerController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public String authenticateUser(@RequestBody UserDTO userData) {           
        boolean success = userService.registerUser(
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
        System.out.println("JAAAAAG BEFINNNEER MIG HÄR");
        String email = user.get("user_email");
        String password = user.get("user_password");

        boolean success = userService.checkIfUserExistsBeforeLogIn(
            email, password);

        if(success) {
            return "true";
        } else {
            return "Login failed!";
        }
    }

    @PostMapping("/CreateInitiative")
    public String createInitiative(@RequestBody InitiativeDTO data) {
        boolean success = userService.createInitiative(
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

    @GetMapping("/FetchInitiatives")
    public List<InitiativeDTO> fetchInitiative() {
        return userService.fetchInitiative();
    }

    @PostMapping("/FetchInitiativeByID")
    @ResponseBody
    public InitiativeDTO fetchInitiativeByID(@RequestBody String id) {
        return userService.fetchInitiativeByID(id);
    }

}
