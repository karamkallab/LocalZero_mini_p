package com.example.localzero.Command;

import com.example.localzero.Controller.DatabaseController;
import com.example.localzero.DTO.InitiativeDTO;

import java.util.List;

public class CommandFetchInitiative implements UserCommand{
    private DatabaseController dbController;
    private String email;

    public CommandFetchInitiative(DatabaseController dbController, String email) {
        this.dbController = dbController;
        this.email = email;
    }

    @Override
    public boolean executeAction() {
        return true;
    }

    @Override
    public List<InitiativeDTO> fetchInitiatives() {
        return dbController.fetchInitiative(email);
    }

    @Override
    public InitiativeDTO fetchInitiativeByID() {
        return null;
    }


}
