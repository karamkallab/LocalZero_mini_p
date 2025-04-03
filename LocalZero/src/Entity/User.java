package Entity;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String email;
    private String location;
    private ArrayList<Role> roles;

    public void addUserRole(Role role) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        roles.add(role);
    }

    public boolean userRoleAlreadyExists(Role role) {
        if (roles == null) {
            return false;
        }
        return roles.contains(role);
    }
}
