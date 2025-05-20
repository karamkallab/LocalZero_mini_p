package com.example.localzero.Controller;

import com.example.localzero.Command.*;
import com.example.localzero.DTO.EcoactionsDTO;
import com.example.localzero.DTO.InitiativeDTO;
import com.example.localzero.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
            String[] visibility, int createdByUserID) {
        UserCommand command = new CommandCreateInitiative(title, description, location, category, visibility, createdByUserID, dbController);
        return command.executeAction();
    }

    public List<InitiativeDTO> fetchInitiative(String email) {
        UserCommand command = new CommandFetchInitiative(dbController, email);
        return command.fetchInitiatives();
    }

    public InitiativeDTO fetchInitiativeByID(String id) {
        UserCommand command = new CommandFetchInitiativeByID(dbController, id);
        command.executeAction();
        return command.fetchInitiativeByID();
    }
    
    //behöver user id
    public boolean updateInitiative(String initiativeID, String title, String description, String location, String category,
                                    String[] visibility, int createdByUserID) {
        UserCommand command = new CommandUpdateInitiative(initiativeID, title, description, location, category,
                visibility, createdByUserID, dbController);
        return command.executeAction();
    }

    public boolean likeInitiative(int userId, int initiativeId) {
        UserCommand likeCommand = new CommandLikeInitiative(dbController, userId, initiativeId);
        return likeCommand.executeAction();
    }

    public int fetchUserIdByEmail(String email) {
        UserCommand command = new CommandFetchUserIdByEmail(dbController, email);
        command.executeAction();
        return command.fetchUserIdByEmail();
    }
    
    public boolean joinInitiative(int userId, int initiativeId) {
        UserCommand joinCommand = new CommandJoinInitiative(dbController, userId, initiativeId);
        return joinCommand.executeAction();
    }

    public boolean checkJoinStatus(int userId, int initiativeId) {
        UserCommand command = new CommandCheckJoinStatus(dbController, userId, initiativeId);
        command.executeAction(); 
        return command.checkJoinStatus(); 
    }
    
    public boolean commentInitiative(int userId, int initiativeId, String comment) {
        UserCommand commentCommand = new CommandCommentInitiative(dbController, userId, initiativeId, comment);
        return commentCommand.executeAction();
    } 

    public List<Map<String, String>> getCommentsByInitiativeId(int initiativeId) {
        UserCommand command = new CommandGetCommentsByInitiativeId(dbController, initiativeId);
        command.executeAction();
        return ((CommandGetCommentsByInitiativeId) command).getComments();
    }    

    public boolean logEcoActions(String action, String category, String date, String userID) {
        UserCommand logEcoActions = new CommandLogEcoActions(dbController, action, category, date, userID);
        return logEcoActions.executeAction();
    }

    public boolean getUserRole(String user_email) {
        UserCommand command = new CommandGetUserRole(dbController, user_email);
        return command.executeAction();
    }

    public boolean giveNewUserRole(String email, String role) {
        UserCommand command = new CommandNewUserRole(dbController, email, role);
        return command.executeAction();
    }

    public boolean unlikeInitiative(int userId, int initiativeId) {
        UserCommand command = new CommandUnlikeInitiative(dbController, userId, initiativeId);
        return command.executeAction();
    }  

    public boolean leaveInitiative(int userId, int initiativeId) {
        UserCommand command = new CommandLeaveInitiative(dbController, userId, initiativeId);
        return command.executeAction();
    }

    public List<EcoactionsDTO> fetchEcoactions(){
        return dbController.FetchEcoactions();
    }

    public String fetchNameIdByEmail(String email) {
        return dbController.fetchNameByEmail(email);
    }

    public String fetchRoleByEmail(String email){
        return dbController.fetchRoleByEmail(email);
    }

    public ArrayList<String> fetchAllName(){
        return dbController.fetchAllName();
    }

    public ArrayList<ChatMessage> loadMessageHistory(String fromUsername, String toUsername){
        return dbController.loadMessageHistory(dbController.fetchIDByName(fromUsername), dbController.fetchIDByName(toUsername));
    }
}
