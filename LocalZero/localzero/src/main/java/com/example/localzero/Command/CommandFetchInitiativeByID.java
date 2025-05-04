package com.example.localzero.Command;

import com.example.localzero.Controller.DatabaseController;
import com.example.localzero.DTO.InitiativeDTO;

import java.util.List;

public class CommandFetchInitiativeByID implements UserCommand{
    private DatabaseController dbController;

    public CommandFetchInitiativeByID(DatabaseController dbController) {
        this.dbController = dbController;
    }

    @Override
    public boolean executeAction() {
        return dbController.fetchInitiativeByIDCheck();
    }



}
