package com.example.localzero.Command;

import com.example.localzero.Controller.DatabaseController;
import com.example.localzero.DTO.InitiativeDTO;
import java.util.List;

public class CommandLikeInitiative implements UserCommand {
    private DatabaseController dbController;
    private int userId;
    private int initiativeId;

    public CommandLikeInitiative(DatabaseController dbController, int userId, int initiativeId) {
        this.dbController = dbController;
        this.userId = userId;
        this.initiativeId = initiativeId;
    }

    @Override
    public boolean executeAction() {
        return dbController.likeInitiative(userId, initiativeId);
    }

}
