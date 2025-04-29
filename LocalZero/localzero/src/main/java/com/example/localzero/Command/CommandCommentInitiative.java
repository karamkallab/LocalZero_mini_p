package com.example.localzero.Command;

import com.example.localzero.Controller.DatabaseController;
import com.example.localzero.DTO.InitiativeDTO;
import java.util.List;

public class CommandCommentInitiative implements UserCommand {
    private DatabaseController dbController;
    private int userId;
    private int initiativeId;
    private String comment;

    public CommandCommentInitiative(DatabaseController dbController, int userId, int initiativeId, String comment) {
        this.dbController = dbController;
        this.userId = userId;
        this.initiativeId = initiativeId;
        this.comment = comment;
    }

    @Override
    public boolean executeAction() {
        return dbController.commentInitiative(userId, initiativeId, comment);
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
