package com.example.localzero.Command;

import com.example.localzero.Controller.DatabaseController;
import com.example.localzero.DTO.InitiativeDTO;
import java.util.List;

public class CommandLeaveInitiative implements UserCommand {
    private DatabaseController dbController;
    private int userId;
    private int initiativeId;

    public CommandLeaveInitiative(DatabaseController dbController, int userId, int initiativeId) {
        this.dbController = dbController;
        this.userId = userId;
        this.initiativeId = initiativeId;
    }

    @Override
    public boolean executeAction() {
        return dbController.leaveInitiative(userId, initiativeId);
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
