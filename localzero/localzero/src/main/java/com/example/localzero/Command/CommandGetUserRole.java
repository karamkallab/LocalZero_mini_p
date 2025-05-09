package com.example.localzero.Command;

import java.util.List;

import com.example.localzero.Controller.DatabaseController;
import com.example.localzero.DTO.InitiativeDTO;

public class CommandGetUserRole implements UserCommand {
    private final DatabaseController dbController;
    private final String user_email;

    public CommandGetUserRole(DatabaseController dbController, String user_email) {
        this.dbController = dbController;
        this.user_email = user_email;
    }

    @Override
    public boolean executeAction() {
        return dbController.getUserRole(user_email);
    }

	@Override
	public List<InitiativeDTO> fetchInitiatives() {
        return null;
	}

	@Override
	public InitiativeDTO fetchInitiativeByID() {
		return null;
	}

}
