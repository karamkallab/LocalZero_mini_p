package com.example.localzero.Controller;

import com.example.localzero.Command.*;
import com.example.localzero.DTO.InitiativeDTO;
import com.example.localzero.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public boolean createInitiative(String title, String description, String location, String category,
            String visibility, int createdByUserID) {
        UserCommand command = new CommandCreateInitiative(title, description, location, category, visibility, createdByUserID, dbController);
        return command.executeAction();
    }

    public List<InitiativeDTO> fetchInitiative(){
        return dbController.fetchInitiative();
    }

    public InitiativeDTO fetchInitiativeByID(String id){
        return dbController.fetchInitiativeByID(id);
    }

    public boolean updateInitiative(InitiativeDTO initiativeDTO) {
        UserCommand command = new CommandUpdateInitiative(dbController, initiativeDTO);
        return command.executeAction();
    }

    public boolean likeInitiative(int userId, int initiativeId) {
        CommandLikeInitiative likeCommand = new CommandLikeInitiative(dbController, userId, initiativeId);
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
    public String fetchNameIdByEmail(String email) {
        return dbController.fetchNameByEmail(email);
    }
    public ArrayList<String> fetchAllName(){
        return dbController.fetchAllName();
    }

    public ArrayList<ChatMessage> loadMessageHistory(String fromUsername, String toUsername){
        return dbController.loadMessageHistory(dbController.fetchIDByName(fromUsername), dbController.fetchIDByName(toUsername));
    }
}
