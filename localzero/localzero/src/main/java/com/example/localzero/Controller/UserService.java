package com.example.localzero.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

import com.example.localzero.Command.CommandCreateInitiative;
import com.example.localzero.Command.CommandLogIn;
import com.example.localzero.Command.CommandRegisterUser;
import com.example.localzero.Command.UserCommand;
// @Service marks this class as a service component in the Spring container.
// It is automatically managed as a singleton and used for business logic.
@Service
public class UserService {

    private final DatabaseController dbController;

    @Autowired
    public UserService(DatabaseController dbController) {
        this.dbController = dbController;
    }

    public boolean registerUser(String name, String email, String password, String location, String role) {
        UserCommand command = new CommandRegisterUser(dbController, name, email, password, location, role);
        return command.executeAction();
    }

    public boolean checkIfUserExistsBeforeLogIn(String email, String password) {
        UserCommand command = new CommandLogIn(dbController, email, password);
        return command.executeAction();
    }

    public boolean createInitiative(String title, String description, String location, String category,
            String visibility) {
        UserCommand command = new CommandCreateInitiative(title, description, location, category, visibility, dbController);
        return command.executeAction();
    }

}
