package com.example.localzero.Entity;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String email;
    private String location;
    private ArrayList<String> roles;

    public void addUserRole(String role) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        roles.add(role);
    }

    public boolean userRoleAlreadyExists(String role) {
        if (roles == null) {
            return false;
        }
        return roles.contains(role);
    }
}
