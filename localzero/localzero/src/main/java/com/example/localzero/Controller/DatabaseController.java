package com.example.localzero.Controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.localzero.DTO.InitiativeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import io.github.cdimascio.dotenv.Dotenv;
import io.netty.handler.codec.http.HttpContentEncoder.Result;

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


    public boolean createInitiative(String title, String description, String location, String category, String[] visibility) {
        CallableStatement stmt = null;
    
        try {
            stmt = conn.prepareCall("call create_initiative(?, ?, ?, ?, ?)");

            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setString(3, location);
            stmt.setString(4, category);
            Array sqlArray = conn.createArrayOf("VARCHAR", visibility);
            stmt.setArray(5, sqlArray);
    
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

}

