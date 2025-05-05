package com.example.localzero.Command;

import java.util.List;

import com.example.localzero.Controller.DatabaseController;
import com.example.localzero.DTO.InitiativeDTO;

public class CommandLogEcoActions implements UserCommand {
    private DatabaseController dbController;
    private String userId;
    private String action;
    private String category;
    private String date;


    public CommandLogEcoActions(DatabaseController dbController, String action, String category, String date, String userID) {
        this.dbController = dbController;
        this.userId = userID;
        this.action = action;
        this.category = category;
        this.date = date;
    }

    @Override
    public boolean executeAction() {
        return dbController.logEcoActions(action, category, date, userId);
    }

    @Override
    public List<InitiativeDTO> fetchInitiatives() {
        return List.of();
    }

    @Override
    public InitiativeDTO fetchInitiativeByID() {
        return null;
    }


}
