package com.example.localzero.Command;

import com.example.localzero.Controller.DatabaseController;
import com.example.localzero.DTO.InitiativeDTO;

import java.util.List;

public class CommandUpdateInitiative implements UserCommand{
    private DatabaseController db;
    private String title;
    private String description;
    private String location;
    private String category;
    private String[] visibility;
    private int createdByUserID;

    public CommandUpdateInitiative(String title, String description, String location, String category, String[] visibility, int createdByUserID, DatabaseController db) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.category = category;
        this.visibility = visibility;
        this.createdByUserID = createdByUserID;
        this.db = db;
    }
    @Override
    public boolean executeAction() {
        return db.updateInitiative(title, description, location, category, visibility, createdByUserID);
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
