package com.example.localzero.Command;

import com.example.localzero.Controller.DatabaseController;
import com.example.localzero.DTO.InitiativeDTO;

import java.util.List;

public class CommandFetchUserIdByEmail implements UserCommand {
    private DatabaseController dbController;
    private String email;
    private int userId;
    

    public CommandFetchUserIdByEmail(DatabaseController dbController, String email) {
        this.dbController = dbController;
        this.email = email;
    }

    @Override
    public boolean executeAction() {
       this.userId = dbController.fetchUserIdByEmail(email);
        return true; 
    }

    @Override
    public List<InitiativeDTO> fetchInitiatives() {
        return List.of(); 
    }

    @Override
    public InitiativeDTO fetchInitiativeByID() {
        return null;
    }

    @Override
    public int fetchUserIdByEmail() {
        return userId;
    }
}
