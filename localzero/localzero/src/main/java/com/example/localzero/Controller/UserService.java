package com.example.localzero.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final DatabaseController dbController;

    @Autowired
    public UserService(DatabaseController dbController) {
        this.dbController = dbController;
    }

    public boolean registerUser(String name, String email, String password, String location, String role) {
        return dbController.registerUser(name, email, password, location, role);
    }

    public boolean checkIfUserExistsBeforeLogIn(String email, String password) {
        return dbController.checkIfUserExistsBeforeLogIn(email, password);
    }

    public boolean createInitiative(String title, String description, String location, String category,
            String visibility) {
        return dbController.createInitiative(title, description, location, category, visibility);
    }




}
