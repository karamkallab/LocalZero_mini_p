package com.example.localzero.Command;

import java.util.List;

import com.example.localzero.Controller.DatabaseController;
import com.example.localzero.DTO.InitiativeDTO;

public class CommandNewUserRole implements UserCommand {
    private final DatabaseController dbController;
    private String user_email;
    private String role;


    public CommandNewUserRole(DatabaseController dbController, String user_email, String role) {
        this.dbController = dbController;
        this.user_email = user_email;
        this.role = role;
    }

    @Override
    public boolean executeAction() {
        return dbController.newUserRole(user_email, role);
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
