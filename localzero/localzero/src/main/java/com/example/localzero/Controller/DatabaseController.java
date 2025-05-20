package com.example.localzero.Controller;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import com.example.localzero.DTO.EcoactionsDTO;
import com.example.localzero.DTO.InitiativeDTO;
import com.example.localzero.chat.ChatMessage;
import com.example.localzero.chat.MessageType;
import com.example.localzero.chat.NotificationInitiatives;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import io.github.cdimascio.dotenv.Dotenv;
import io.netty.handler.codec.http.HttpContentEncoder.Result;

// @Repository is used for classes that handle communication with the database.
@Repository
public class DatabaseController {
    private Connection conn;
    private DatabaseConnection dbConnection;
    @Autowired
    private NotificationService notificationService;

    //@Autowired is used to automatically inject (provide) an instance of 
    // a class that is managed by Spring.
    @Autowired
    public DatabaseController(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.conn = dbConnection.getConnection();
    }

    public boolean registerUser(String name, String email, String password, String location, String role) {
        CallableStatement stmt = null;
        String[] array = role.split(", ");

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


    public boolean createInitiative(String title, String description, String location, String category, String[] visibility, int createdByUserID) {
        CallableStatement stmt = null;
    
        try {
            stmt = conn.prepareCall("call create_initiative(?, ?, ?, ?, ?, ?)");

            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setString(3, location);
            stmt.setString(4, category);
            Array sqlArray = conn.createArrayOf("VARCHAR", visibility);
            stmt.setArray(5, sqlArray);
            stmt.setInt(6, createdByUserID);
    
            stmt.executeUpdate();

            NotificationInitiatives notificationInitiatives = new NotificationInitiatives();
            notificationInitiatives.setContent(title);
            notificationInitiatives.setSender(createdByUserID);
            notificationInitiatives.setType(MessageType.INI_NOTIS);
            notificationService.sendInitiativeNotification(notificationInitiatives);

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

    public List<InitiativeDTO> fetchInitiative(String email){
        List<InitiativeDTO> initiatives = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM getPublicInitiatives()");
            rs = stmt.executeQuery();

            while (rs.next()) {
                InitiativeDTO initiative = new InitiativeDTO();
                initiative.setID(String.valueOf(rs.getInt("id")));
                initiative.setTitle(rs.getString("title"));
                initiative.setDescription(rs.getString("description"));
                initiative.setLocation(rs.getString("location"));
                initiative.setCategory(rs.getString("category"));
                initiative.setVisibility(rs.getString("visibility"));
                initiatives.add(initiative);
            }

                List<String> userRoles = new ArrayList<>();
                stmt = conn.prepareStatement("SELECT unnest(getUserRoles(?))");
                stmt.setString(1, email);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    userRoles.add(rs.getString(1));
                }

                for (String role : userRoles) {
                stmt = conn.prepareStatement("SELECT * FROM getInitiativesByRole(?)");
                stmt.setString(1, role);
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

        Set<String> seenIds = new HashSet<>();
        List<InitiativeDTO> finalinitiatives = new ArrayList<>();

        for (InitiativeDTO initiative : initiatives) {
            if (seenIds.add(initiative.getId())) {
                finalinitiatives.add(initiative);
            }
        }

        //Sort the finalinitiatives so the newest initiaves come first
        for (int i = 0; i < finalinitiatives.size() - 1; i++) {
            for (int j = 0; j < finalinitiatives.size() - i - 1; j++) {
                int id1 = Integer.parseInt(finalinitiatives.get(j).getId());
                int id2 = Integer.parseInt(finalinitiatives.get(j + 1).getId());

                if (id1 < id2) {
                    InitiativeDTO temp = finalinitiatives.get(j);
                    finalinitiatives.set(j, finalinitiatives.get(j + 1));
                    finalinitiatives.set(j + 1, temp);
                }
            }
        }

        return finalinitiatives;
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

    public boolean updateInitiative(String initiativeID, String title, String description, String location, String category, String[] visibility, int createdByUserID){
        PreparedStatement stmt = null;

        try {
            String sql = "UPDATE initiatives SET title = ?, description = ?, location = ?, category = ?, visibility = ? WHERE id = ?::int";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setString(3, location);
            stmt.setString(4, category);
            Array sqlArray = conn.createArrayOf("VARCHAR", visibility);
            stmt.setArray(5, sqlArray);
            stmt.setInt(6, Integer.parseInt(initiativeID));

            int affectedRows = stmt.executeUpdate();

            NotificationInitiatives notificationInitiatives = new NotificationInitiatives();
            notificationInitiatives.setContent(title);
            notificationInitiatives.setSender(createdByUserID);
            notificationInitiatives.setType(MessageType.UPDATE_NOTIS);
            notificationService.sendInitiativeNotification(notificationInitiatives);

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

    public boolean logEcoActions(String action, String category, String date, String userId) {
        CallableStatement stmt = null;

        try {
            stmt = conn.prepareCall("CALL log_eco_actions(?, ?, ?, ?)");
    
            stmt.setString(1, action);
            stmt.setString(2, category);
            stmt.setDate(3, Date.valueOf(date)); // Assuming date is in YYYY-MM-DD format
            stmt.setInt(4, Integer.parseInt(userId));
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

    public boolean commentInitiative(int userId, int initiativeId, String comment) {
        PreparedStatement stmt = null;
        try {
            String sql = "INSERT INTO initiative_comments (user_id, initiative_id, comment) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, initiativeId);
            stmt.setString(3, comment);
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

    public List<Map<String, String>> fetchCommentsByInitiativeId(int initiativeId) {
        List<Map<String, String>> comments = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT c.comment, c.created_at, u.name " +
            "FROM initiative_comments c " +
            "JOIN users u ON c.user_id = u.user_id " +
            "WHERE c.initiative_id = ? " +
            "ORDER BY c.created_at DESC";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, initiativeId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, String> commentMap = new HashMap<>();
                commentMap.put("comment", rs.getString("comment"));
                commentMap.put("created_at", rs.getTimestamp("created_at").toString());
                commentMap.put("name", rs.getString("name"));

                comments.add(commentMap);
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

        return comments;
    }
    public boolean getUserRole(String user_email) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;

        try {
            String sql = "SELECT isCommunityOrganizer(?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user_email);
            rs = stmt.executeQuery();

            if (rs.next()) {
                result = rs.getBoolean(1);

                return result;
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

        return false;
    }

    public boolean newUserRole(String user_email, String role) {
        CallableStatement stmt = null;
        if(role.equals("Community Organizer")){
            role = "Comunity Organizer";
        }

        try {
            stmt = conn.prepareCall("CALL giveUserNewRole(?, ?)");

            stmt.setString(1, user_email);
            stmt.setString(2, role);
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

    public boolean unlikeInitiative(int userId, int initiativeId) {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE FROM initiative_likes WHERE user_id = ? AND initiative_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, initiativeId);
            int rows = stmt.executeUpdate();
            return rows > 0;
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

    public boolean leaveInitiative(int userId, int initiativeId) {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE FROM initiative_participants WHERE user_id = ? AND initiative_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, initiativeId);
            int rows = stmt.executeUpdate();
            return rows > 0;
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

    public List<EcoactionsDTO> FetchEcoactions() {
        List<EcoactionsDTO> ecoActionsList = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT id, user_id, action, category, date_submitted, co2_savings FROM ecoActions");
            rs = stmt.executeQuery();
            while (rs.next()) {
                EcoactionsDTO ecoAction = new EcoactionsDTO();
                ecoAction.setId(rs.getInt("id"));
                ecoAction.setUserId(rs.getInt("user_id"));
                ecoAction.setAction(rs.getString("action"));
                ecoAction.setCategory(rs.getString("category"));
                ecoAction.setDateSubmitted(rs.getDate("date_submitted"));

                ecoActionsList.add(ecoAction);
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


        return ecoActionsList;
    }

    public int fetchIDByName(String name) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int id = 0;

        try {
            // Corrected query without quotes around the placeholder
            String sql = "SELECT user_id FROM users WHERE name = ?;";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);  // Correctly binds the name parameter
            rs = stmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("user_id");
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

        return id;
    }

    public void saveMessage(int fromUserId, int toUserId, String message) {
        PreparedStatement stmt = null;
        try {
            String sql = "INSERT INTO chat_history (from_user_id, to_user_id, message) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, fromUserId);
            stmt.setInt(2, toUserId);
            stmt.setString(3, message);
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public String fetchNameByEmail(String email){
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

    public String fetchRoleByEmail(String email){
        StringBuilder role = new StringBuilder();

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT user_role FROM user_roles WHERE user_email = ?;";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            while (rs.next()) {
                if (!role.isEmpty()) {
                    role.append(", ");
                }
                role.append(rs.getString("user_role"));
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
        return role.toString();
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

    public ArrayList<ChatMessage> loadMessageHistory(int fromUserId, int toUserId) {
        ArrayList<ChatMessage> messages = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM chat_history " +
                    "WHERE (from_user_id = ? AND to_user_id = ?) " +
                    "   OR (from_user_id = ? AND to_user_id = ?) " +
                    "ORDER BY message_id ASC";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, fromUserId);
            stmt.setInt(2, toUserId);
            stmt.setInt(3, toUserId); // reversed direction
            stmt.setInt(4, fromUserId);

            rs = stmt.executeQuery();
            while (rs.next()) {
                int from = rs.getInt("from_user_id");
                int to = rs.getInt("to_user_id");
                String msg = rs.getString("message");
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setSender(fetchNameByID(from));
                chatMessage.setRecipient(fetchNameByID(to));
                chatMessage.setContent(msg);
                messages.add(chatMessage);
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

        return messages;
    }

    public String fetchNameByID(int id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String name = "";

        try {
            String sql = "SELECT name FROM users WHERE user_id = ?;";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
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

}

