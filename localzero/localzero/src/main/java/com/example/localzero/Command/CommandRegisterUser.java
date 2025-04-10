package com.example.localzero.Command;

import javax.xml.crypto.Data;

import com.example.localzero.UserCommand;
import com.example.localzero.Controller.DatabaseController;

public class CommandRegisterUser implements UserCommand {
    private DatabaseController dbController;
    private String name;
    private String email;
    private String password;
    private String location;
    private String role;

    public CommandRegisterUser(DatabaseController dbController, String name, String email, String password, String location, String role) {
        this.dbController = dbController;
        this.name = name;
        this.email = email;
        this.password = password;
        this.location = location;
        this.role = role;
    }

    @Override
    public boolean executeAction() {
        return dbController.registerUser(name, email, password, location, role);
    }
}
