package com.example.localzero.Command;

import com.example.localzero.Controller.DatabaseController;
import com.example.localzero.DTO.InitiativeDTO;

import java.util.List;

public class CommandUpdateInitiative implements UserCommand{
    private DatabaseController dbController;
    private InitiativeDTO initiativeDTO;

    public CommandUpdateInitiative(DatabaseController dbController, InitiativeDTO initiativeDTO) {
        this.dbController = dbController;
        this.initiativeDTO = initiativeDTO;
    }
    @Override
    public boolean executeAction() {
        return dbController.updateInitiative(initiativeDTO);
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
