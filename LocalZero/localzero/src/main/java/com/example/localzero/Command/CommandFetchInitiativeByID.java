package com.example.localzero.Command;

import com.example.localzero.Controller.DatabaseController;
import com.example.localzero.DTO.InitiativeDTO;

import java.util.List;

public class CommandFetchInitiativeByID implements UserCommand{
    private DatabaseController dbController;
    private String id;
    private InitiativeDTO initiative;

    public CommandFetchInitiativeByID(DatabaseController dbController, String id) {   
        this.dbController = dbController;
        this.id = id;
    }

    @Override
    public boolean executeAction() {
        this.initiative = dbController.fetchInitiativeByID(id);
        return true;
    }

    @Override
    public List<InitiativeDTO> fetchInitiatives() {
        return List.of();
    }

    @Override
    public InitiativeDTO fetchInitiativeByID() {
        return initiative;
    }
}
