package com.example.localzero.Controller;

import java.sql.*;
import io.github.cdimascio.dotenv.Dotenv;


public class DatabaseController {
    private ServerController controller;
    private Connection conn;
    private DatabaseConnection dbConnection;

    public DatabaseController(ServerController controller) {
        this.controller = controller;
        this.dbConnection = DatabaseConnection.getInstance();

        if(dbConnection != null) {
            this.conn = dbConnection.getConnection();
        }
    }

    public boolean registerUser(String name, String email, String password, String location, String role) {
        CallableStatement stmt = null;
        String[] array = role.split(",");

        try {
            stmt = conn.prepareCall("CALL new_register_user(?, ?, ?, ?)");
    
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, location);
    
            stmt.executeUpdate();

            addRole(email, array[0]);
            if(array.length == 2) {
                addRole(email, array[1]);
            }
    
            return true;
    
        } catch (Exception e) {
            e.printStackTrace();
            return false;
    
        } finally {
            try {
                if (stmt != null) stmt.close();  // ✅ Stäng bara statement
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkIfUserExistsBeforeLogIn(String email, String password) {
        CallableStatement stmt = null;
        int result = -1;

        try{
            stmt = conn.prepareCall("{ ? = call check_if_user_exists(?, ?) }");
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.execute();
            result = stmt.getInt(1);


            if(result == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
    
        } finally {
            try {
                if (stmt != null) stmt.close();  // ✅ Stäng bara statement
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean addRole(String name, String role){
        CallableStatement stmt = null;

        try {
            stmt = conn.prepareCall("CALL add_onerole(?, ?)");
    
            stmt.setString(1, role);
            stmt.setString(2, name);
            stmt.executeUpdate();
    
            return true;
    
        } catch (Exception e) {
            e.printStackTrace();
            return false;
    
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean createInitiative(String title, String description, String location, String category, String visibility) {
        CallableStatement stmt = null;
    
        try {
            //stmt = conn.prepareCall("CALL create_initiative(?, ?, ?, ?, ?)");
            stmt = conn.prepareCall("CALL create_initiative(?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar)");


            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setString(3, location);
            stmt.setString(4, category);
            stmt.setString(5, visibility);
    
            stmt.executeUpdate();
            return true;
    
        } catch (Exception e) {
            e.printStackTrace();
            return false;
    
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
}

