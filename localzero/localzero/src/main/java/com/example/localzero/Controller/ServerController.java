package com.example.localzero.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/eco-actions-log")
    public boolean logEcoAcions(@RequestBody Map<String, Object> data) {
        String action = (String) data.get("action");
        String category = (String) data.get("category");
        String date = (String) data.get("date");
        String userID = (String) data.get("userId");

        boolean success = userService.logEcoActions(action, category, date, userID);
        return success;
    }


    @PostMapping("/authenticator")
    public Map<String, Object> loginUser(@RequestBody HashMap<String, String> user) {
        System.out.println("JAG BEFINNER MIG HÄR");
        String email = user.get("user_email");
        String password = user.get("user_password");
    
        boolean success = userService.checkIfUserExistsBeforeLogIn(email, password);
        Map<String, Object> response = new HashMap<>();
    
        if (success) {
            int userId = userService.fetchUserIdByEmail(email); // hämtar userId från email
            response.put("success", true);
            response.put("userId", userId);
        } else {
            response.put("success", false);
        }
        return response;
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

    @PostMapping("/UpdateInitiative")
    public String updateInitiative(@RequestBody InitiativeDTO initiative) {
        boolean success = userService.updateInitiative(initiative);
        if (success) {
            return "Initiative updated!";
        } else {
            return "Something went wrong.";
        }
    }

    @PostMapping("/LikeInitiative")
    public String likeInitiative(@RequestBody Map<String, Integer> data) {
        int userId = data.get("userId");
        int initiativeId = data.get("initiativeId");
        boolean success = userService.likeInitiative(userId, initiativeId);

        if (success) {
            return "Liked successfully!";
        } else {
            return "Already liked or error.";
        }
    }

@PostMapping("/JoinInitiative")
public String joinInitiative(@RequestBody Map<String, Integer> data) {
    int userId = data.get("userId");
    int initiativeId = data.get("initiativeId");
    boolean success = userService.joinInitiative(userId, initiativeId);

    if (success) {
        return "Joined successfully!";
    } else {
        return "Already joined or error.";
    }
}

@PostMapping("/CheckJoinStatus")
public boolean checkJoinStatus(@RequestBody Map<String, Integer> data) {
    int userId = data.get("userId");
    int initiativeId = data.get("initiativeId");
    return userService.checkJoinStatus(userId, initiativeId);
}

}
