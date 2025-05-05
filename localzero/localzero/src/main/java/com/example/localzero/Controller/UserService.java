package com.example.localzero.Controller;

import com.example.localzero.Command.*;
import com.example.localzero.DTO.InitiativeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

// @Service marks this class as a service component in the Spring container.
// It is automatically managed as a singleton and used for business logic.
@Service
public class UserService {

    private final DatabaseController dbController;

    @Autowired
    public UserService(DatabaseController dbController) {
        this.dbController = dbController;
    }

    public boolean registerUser(String name, String email, String password, String location, String role) {
        UserCommand command = new CommandRegisterUser(dbController, name, email, password, location, role);
        return command.executeAction();
    }

    public boolean checkIfUserExistsBeforeLogIn(String email, String password) {
        UserCommand command = new CommandLogIn(dbController, email, password);
        return command.executeAction();
    }

    //behöver user id
    public boolean createInitiative(String title, String description, String location, String category,
            String visibility) {
        UserCommand command = new CommandCreateInitiative(title, description, location, category, visibility, dbController);
        return command.executeAction();
    }

    public List<InitiativeDTO> fetchInitiative() {
        UserCommand command = new CommandFetchInitiative(dbController);
        return command.fetchInitiatives();
    }

    public InitiativeDTO fetchInitiativeByID(String id) {
        UserCommand command = new CommandFetchInitiativeByID(dbController, id);
        return command.fetchInitiativeByID();
    }

    //behöver user id
    public boolean updateInitiative(InitiativeDTO initiativeDTO) {
        UserCommand command = new CommandUpdateInitiative(dbController, initiativeDTO);
        return command.executeAction();
    }

    public boolean likeInitiative(int userId, int initiativeId) {
        UserCommand likeCommand = new CommandLikeInitiative(dbController, userId, initiativeId);
        return likeCommand.executeAction();
    }

    public int fetchUserIdByEmail(String email) {
        return dbController.fetchUserIdByEmail(email);
    }
    public boolean joinInitiative(int userId, int initiativeId) {
        CommandJoinInitiative joinCommand = new CommandJoinInitiative(dbController, userId, initiativeId);
        return joinCommand.executeAction();
    }

    public boolean checkJoinStatus(int userId, int initiativeId) {
        return dbController.checkJoinStatus(userId, initiativeId);
    }  

    public boolean commentInitiative(int userId, int initiativeId, String comment) {
        CommandCommentInitiative commentCommand = new CommandCommentInitiative(dbController, userId, initiativeId, comment);
        return commentCommand.executeAction();
    } 

    public List<Map<String, String>> getCommentsByInitiativeId(int initiativeId) {
    return dbController.fetchCommentsByInitiativeId(initiativeId);
}

    

    
    public boolean logEcoActions(String action, String category, String date, String userID) {
        UserCommand logEcoActions = new CommandLogEcoActions(dbController, action, category, date, userID);
        return logEcoActions.executeAction();
    } 
        
}
