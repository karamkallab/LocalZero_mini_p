package com.example.localzero.Command;

import com.example.localzero.UserCommand;
import com.example.localzero.Controller.DatabaseController;

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

    
}
