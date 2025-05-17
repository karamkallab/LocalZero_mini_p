package com.example.localzero.Command;

import com.example.localzero.Controller.DatabaseController;
import com.example.localzero.DTO.InitiativeDTO;

import java.util.List;
import java.util.Map;

public class CommandGetCommentsByInitiativeId implements UserCommand {
    private final DatabaseController dbController;
    private final int initiativeId;
    private List<Map<String, String>> comments;

    public CommandGetCommentsByInitiativeId(DatabaseController dbController, int initiativeId) {
        this.dbController = dbController;
        this.initiativeId = initiativeId;
    }

    @Override
    public boolean executeAction() {
        this.comments = dbController.fetchCommentsByInitiativeId(initiativeId);
        return true;
    }

    public List<Map<String, String>> getComments() {
        return comments;
    }

    @Override public List<InitiativeDTO> fetchInitiatives() { 
        return List.of(); 
    }
    @Override public InitiativeDTO fetchInitiativeByID() { 
        return null; 
    }
  
}
