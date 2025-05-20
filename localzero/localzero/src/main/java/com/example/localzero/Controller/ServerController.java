package com.example.localzero.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.localzero.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.localzero.DTO.EcoactionsDTO;
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

    @PostMapping("/new-role")
    public boolean giveNewUserRole(@RequestBody Map<String, String> data) {
        String email = data.get("email");
        String role = data.get("role");

        boolean success = userService.giveNewUserRole(email, role);
        return success;
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

    @GetMapping("/user-role")
    public ResponseEntity<Boolean> getUserRole(@RequestParam String email) {
        boolean isCommunityOrganizer = userService.getUserRole(email);

        if(isCommunityOrganizer) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
        }
        
    }



    @PostMapping("/authenticator")
    public Map<String, Object> loginUser(@RequestBody HashMap<String, String> user) {
        String email = user.get("user_email");
        String password = user.get("user_password");
    
        boolean success = userService.checkIfUserExistsBeforeLogIn(email, password);
        Map<String, Object> response = new HashMap<>();
    
        if (success) {
            int userId = userService.fetchUserIdByEmail(email); // hämtar userId från email
            String name = userService.fetchNameIdByEmail(email);
            String role = userService.fetchRoleByEmail(email);

            response.put("success", true);
            response.put("userId", userId);
            response.put("name", name);
            response.put("role", role);
        } else {
            response.put("success", false);
        }
        return response;
    }
    

    @PostMapping("/CreateInitiative")
    public String createInitiative(@RequestBody InitiativeDTO data) {
        String regex = "\\s*,\\s*";
        String visibility = data.getVisibility();
        
        String[] visibilityList = visibility.split(regex);

        for(int i = 0; i < visibilityList.length; i++) {
            if(visibilityList[i].equals("Community Organizer")) {
                visibilityList[i] = "Comunity Organizer";
            }
        }

        boolean success = userService.createInitiative(
            data.getTitle(),
            data.getDescription(),
            data.getLocation(),
            data.getCategory(),
            visibilityList,
            data.getCreatedByUserID()
        );

        if (success) {
            return "Initiative created!";
        } else {
            return "Something went wrong.";
        }
    }

    @GetMapping("/FetchInitiatives")
    public List<InitiativeDTO> fetchInitiative(@RequestParam String email) {
        return userService.fetchInitiative(email);
    }

    @PostMapping("/FetchInitiativeByID")
    @ResponseBody
    public InitiativeDTO fetchInitiativeByID(@RequestBody String id) {
        return userService.fetchInitiativeByID(id);
    }

    @PostMapping("/UpdateInitiative")
    public String updateInitiative(@RequestBody InitiativeDTO initiative) {
        String regex = "\\s*,\\s*";
        String visibility = initiative.getVisibility();

        String[] visibilityList = visibility.split(regex);

        for(int i = 0; i < visibilityList.length; i++) {
            if(visibilityList[i].equals("Community Organizer")) {
                visibilityList[i] = "Comunity Organizer";
            }
        }
        boolean success = userService.updateInitiative(
                initiative.getId(),
                initiative.getTitle(),
                initiative.getDescription(),
                initiative.getLocation(),
                initiative.getCategory(),
                visibilityList,
                initiative.getCreatedByUserID());
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

    @PostMapping("/CommentInitiative")
    public String commentInitiative(@RequestBody Map<String, String> data) {
        int userId = Integer.parseInt(data.get("userId"));
        int initiativeId = Integer.parseInt(data.get("initiativeId"));
        String comment = data.get("comment");

        boolean success = userService.commentInitiative(userId, initiativeId, comment);

        if (success) {
            return "Comment added successfully!";
        } else {
            return "Failed to add comment.";
        }
    }

    @PostMapping("/GetCommentsByInitiativeId")
    public List<Map<String, String>> getComments(@RequestBody Map<String, Integer> data) {
        int initiativeId = data.get("initiativeId");
        return userService.getCommentsByInitiativeId(initiativeId);
    }

    @PostMapping("/UnlikeInitiative")
    public String unlike(@RequestBody Map<String, Integer> data) {
        int userId = data.get("userId");
        int initiativeId = data.get("initiativeId");

        boolean success = userService.unlikeInitiative(userId, initiativeId);
        return success ? "Like removed!" : "No like to remove.";
    }

    @PostMapping("/LeaveInitiative")
    public String leaveInitiative(@RequestBody Map<String, Integer> data) {
        int userId = data.get("userId");
        int initiativeId = data.get("initiativeId");

        boolean success = userService.leaveInitiative(userId, initiativeId);
        return success ? "Left initiative!" : "Error leaving.";
    }

    @GetMapping("/ecoactions")
    public List<EcoactionsDTO> fetchEcoactions() {
      return userService.fetchEcoactions();
    }

    @GetMapping("/FetchAllName")
    public List<String> fetchAllName() {
        ArrayList<String> nameList = userService.fetchAllName();
        return nameList;
    }

    @PostMapping("/LoadMessageHistory")
    @ResponseBody
    public ArrayList<ChatMessage> loadMessageHistory(@RequestBody String usersReceived) {
        ;

        String[] parts = usersReceived.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Expected two usernames separated by a comma");
        }

        String fromUsername = parts[0];
        String toUsername = parts[1];

        return userService.loadMessageHistory(fromUsername, toUsername);
    }


}
