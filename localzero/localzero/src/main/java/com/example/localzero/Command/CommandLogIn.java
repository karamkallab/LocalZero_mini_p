package com.example.localzero.Command;

import com.example.localzero.Controller.DatabaseController;
import com.example.localzero.DTO.InitiativeDTO;

import java.util.List;

public class CommandLogIn implements UserCommand {
    private DatabaseController dbController;
    private String email;
    private String password;

    public CommandLogIn(DatabaseController dbController, String email, String password) {
        this.dbController = dbController;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean executeAction() {
        return dbController.checkIfUserExistsBeforeLogIn(email, password);
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
