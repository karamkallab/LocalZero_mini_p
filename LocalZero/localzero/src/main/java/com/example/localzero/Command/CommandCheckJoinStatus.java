package com.example.localzero.Command;

import com.example.localzero.Controller.DatabaseController;
import com.example.localzero.DTO.InitiativeDTO;

import java.util.List;

public class CommandCheckJoinStatus implements UserCommand {
    private final DatabaseController dbController;
    private final int userId;
    private final int initiativeId;
    private boolean isJoined;

    public CommandCheckJoinStatus(DatabaseController dbController, int userId, int initiativeId) {
        this.dbController = dbController;
        this.userId = userId;
        this.initiativeId = initiativeId;
    }

    @Override
    public boolean executeAction() {
        this.isJoined = dbController.checkJoinStatus(userId, initiativeId);
        return true;
    }

    @Override public List<InitiativeDTO> fetchInitiatives() { 
        return List.of(); 
    }


    @Override public InitiativeDTO fetchInitiativeByID() { 
        return null; 
    }

    @Override
    public boolean checkJoinStatus() {
        return isJoined;
    }
   
}
