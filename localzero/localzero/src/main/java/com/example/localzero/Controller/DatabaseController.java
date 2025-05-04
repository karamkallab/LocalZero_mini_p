package com.example.localzero.Controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.example.localzero.Command.CommandFetchAllUser;
import com.example.localzero.DTO.InitiativeDTO;
import com.example.localzero.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import io.github.cdimascio.dotenv.Dotenv;

// @Repository is used for classes that handle communication with the database.
@Repository
public class DatabaseController {
    private Connection conn;
    private DatabaseConnection dbConnection;

    //@Autowired is used to automatically inject (provide) an instance of 
    // a class that is managed by Spring.
    @Autowired
    public DatabaseController(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.conn = dbConnection.getConnection();
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

            System.out.println("Lyckades skapa användare");
    
            return true;
    
        } catch (Exception e) {
            e.printStackTrace();
            return false;
    
        } finally {
            try {
                if (stmt != null) stmt.close();  // Stäng bara statement
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
                if (stmt != null) stmt.close();  // Stäng bara statement
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
            stmt = conn.prepareCall("call create_initiative(?, ?, ?, ?, ?)");

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
    public boolean fetchInitiativeCheck() {
        List<InitiativeDTO> initiatives = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement("SELECT id, title, description, location, category, visibility FROM initiatives");
            rs = stmt.executeQuery();

            while (rs.next()) {
                InitiativeDTO initiative = new InitiativeDTO();
                initiative.setID(rs.getString("id"));
                initiative.setTitle(rs.getString("title"));
                initiative.setDescription(rs.getString("description"));
                initiative.setLocation(rs.getString("location"));
                initiative.setCategory(rs.getString("category"));
                initiative.setVisibility(rs.getString("visibility"));
                initiatives.add(initiative);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return !initiatives.isEmpty();
    }


    public List<InitiativeDTO> fetchInitiative(){
        List<InitiativeDTO> initiatives = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT id, title, description, location, category, visibility FROM initiatives");
            rs = stmt.executeQuery();

            while (rs.next()) {
                InitiativeDTO initiative = new InitiativeDTO();
                initiative.setID(rs.getString("id"));
                initiative.setTitle(rs.getString("title"));
                initiative.setDescription(rs.getString("description"));
                initiative.setLocation(rs.getString("location"));
                initiative.setCategory(rs.getString("category"));
                initiative.setVisibility(rs.getString("visibility"));
                initiatives.add(initiative);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return initiatives;
    }

    public boolean fetchInitiativeByIDCheck() {
        return true;
    }

    public InitiativeDTO fetchInitiativeByID(String id) {
        InitiativeDTO initiative = new InitiativeDTO();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(
                    "SELECT id, title, description, location, category, visibility FROM initiatives WHERE id = ?::int"
            );
            stmt.setString(1, String.valueOf(id));
            System.out.println(stmt.toString());
            rs = stmt.executeQuery();

            if (rs.next()) {

                initiative.setID(rs.getString("id"));
                initiative.setTitle(rs.getString("title"));
                initiative.setDescription(rs.getString("description"));
                initiative.setLocation(rs.getString("location"));
                initiative.setCategory(rs.getString("category"));
                initiative.setVisibility(rs.getString("visibility"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return initiative;
    }

    public boolean updateInitiative(InitiativeDTO initiative){
        PreparedStatement stmt = null;

        try {
            String sql = "UPDATE initiatives SET title = ?, description = ?, location = ?, category = ?, visibility = ? WHERE id = ?::int";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, initiative.getTitle());
            stmt.setString(2, initiative.getDescription());
            stmt.setString(3, initiative.getLocation());
            stmt.setString(4, initiative.getCategory());
            stmt.setString(5, initiative.getVisibility());
            stmt.setInt(6, Integer.parseInt(initiative.getId()));

            int affectedRows = stmt.executeUpdate();

            return affectedRows > 0;

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

    public boolean likeInitiative(int userId, int initiativeId) {
        PreparedStatement stmt = null;
        try {
            String sql = "INSERT INTO initiative_likes (user_id, initiative_id) VALUES (?, ?) ON CONFLICT (user_id, initiative_id) DO NOTHING";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, initiativeId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
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

    public int fetchUserIdByEmail(String email) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int userId = -1;
    
        try {
            String sql = "SELECT user_id FROM users WHERE email = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
    
            if (rs.next()) {
                userId = rs.getInt("user_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        return userId;
    }

    public boolean joinInitiative(int userId, int initiativeId) {
        PreparedStatement stmt = null;
    
        try {
            String sql = "INSERT INTO initiative_participants (user_id, initiative_id) VALUES (?, ?) ON CONFLICT (user_id, initiative_id) DO NOTHING";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, initiativeId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
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

    public boolean checkJoinStatus(int userId, int initiativeId) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean joined = false;
    
        try {
            String sql = "SELECT 1 FROM initiative_participants WHERE user_id = ? AND initiative_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, initiativeId);
            rs = stmt.executeQuery();
    
            if (rs.next()) {
                joined = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        return joined;
    }

    public String fetchNameIdByEmail(String email){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String name = "";

        try {
            String sql = "SELECT name FROM users WHERE email = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            if (rs.next()) {
                name = rs.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return name;
    }

    public boolean fetchAllNameCheck(){
        List<String> nameList = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT name FROM users");
            rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                nameList.add(name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(!nameList.isEmpty()){
            return true;
        }
        else {
            return false;
        }
    }

    public ArrayList<String> fetchAllName(){
        ArrayList<String> nameList = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT name FROM users");
            rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                nameList.add(name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return nameList;
    }
}

