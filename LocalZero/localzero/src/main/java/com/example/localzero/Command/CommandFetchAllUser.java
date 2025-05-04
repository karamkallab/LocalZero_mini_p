package com.example.localzero.Command;

import com.example.localzero.Controller.DatabaseController;
import com.example.localzero.DTO.InitiativeDTO;

import java.util.ArrayList;
import java.util.List;

public class CommandFetchAllUser implements UserCommand{
    private DatabaseController dbController;

    public CommandFetchAllUser(DatabaseController dbController){
        this.dbController = dbController;
    }
    @Override
    public boolean executeAction() {
        return dbController.fetchAllNameCheck();
    }


}
