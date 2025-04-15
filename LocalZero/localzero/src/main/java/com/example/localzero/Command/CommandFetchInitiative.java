package com.example.localzero.Command;

import com.example.localzero.Controller.DatabaseController;
import com.example.localzero.DTO.InitiativeDTO;

import java.util.List;

public class CommandFetchInitiative implements UserCommand{
    private DatabaseController dbController;

    public CommandFetchInitiative(DatabaseController dbController) {
        this.dbController = dbController;
    }

    @Override
    public boolean executeAction() {

        return true;
    }

    @Override
    public List<InitiativeDTO> fetchDatabase() {
        return dbController.fetchInitiative();
    }


}
