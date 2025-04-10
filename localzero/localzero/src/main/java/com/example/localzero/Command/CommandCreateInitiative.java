package com.example.localzero.Command;

import com.example.localzero.Controller.DatabaseController;

public class CommandCreateInitiative implements UserCommand {
    private DatabaseController db;
    private String title;
    private String description;
    private String location;
    private String category;
    private String visibility;

    public CommandCreateInitiative(String title, String description, String location, String category, String visibility, DatabaseController db) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.category = category;
        this.visibility = visibility;
        this.db = db;
    }



    @Override
    public boolean executeAction() {
        return db.createInitiative(title, description, location, category, visibility);
    }
    
}
