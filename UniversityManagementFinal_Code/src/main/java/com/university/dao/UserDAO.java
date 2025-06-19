package com.university.dao;

import com.university.model.Role;
import com.university.model.User;
import com.university.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User entities.
 * Handles all database operations related to users.
 */
public class UserDAO {

    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return extractUserFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error getting user by ID:");
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return extractUserFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error getting user by username:");
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByUsernameAndRole(String username, Role role) {
        String sql = "SELECT * FROM users WHERE username = ? AND role = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, role.name());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return extractUserFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error getting user by username and role:");
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all users:");
            e.printStackTrace();
        }

        return users;
    }

    public List<User> getUsersByRole(Role role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(extractUserFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving users by role:");
            e.printStackTrace();
        }

        return users;
    }

    public int insertUser(User user) {
        String sql = "INSERT INTO users (username, password, full_name, email, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashPassword(user.getPassword()));  // Use hashed password
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getRole().name());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) return -1;

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    user.setId(generatedId);
                    return generatedId;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error inserting user:");
            e.printStackTrace();
        }

        return -1;
    }

    public int createUser(User user) {
        return insertUser(user);
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, full_name = ?, email = ?, role = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashPassword(user.getPassword()));  // Use hashed password
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getRole().name());
            stmt.setInt(6, user.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating user:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting user:");
            e.printStackTrace();
            return false;
        }
    }

    public User authenticateUser(String username, String password, Role role) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, hashPassword(password));  // Compare hashed password
            stmt.setString(3, role.name());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return extractUserFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error authenticating user:");
            e.printStackTrace();
        }

        return null;
    }

    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hashPassword(newPassword));  // Hash the new password
            stmt.setInt(2, userId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating password:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Extracts a User object from the current row of the ResultSet.
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setRole(Role.valueOf(rs.getString("role")));
        return user;
    }

    /**
     * Placeholder for password hashing (should use a secure hash algorithm like BCrypt).
     */
    private String hashPassword(String password) {
        // Replace with actual hashing in production (e.g., BCrypt)
        return password; // plain password for now (INSECURE!)
    }


      public List<User> getAllStudentsWithProjects() {
        List<User> students = new ArrayList<>();
        String sql = "SELECT u.id AS student_id, u.full_name AS student_name, u.email AS student_email, " +
                     "p.title AS project_title, p.supervisor_name AS supervisor_name, p.status AS project_status " +
                     "FROM users u " +
                     "JOIN student_projects sp ON u.id = sp.student_id " +
                     "JOIN projects p ON sp.project_id = p.id " +
                     "WHERE u.role = 'STUDENT'";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Create User object for the student
                User student = new User();
                student.setId(rs.getInt("student_id"));
                student.setFullName(rs.getString("student_name"));
                student.setEmail(rs.getString("student_email"));
                // Set project details
                student.setProjectName(rs.getString("project_title"));
                student.setSupervisorName(rs.getString("supervisor_name"));
                student.setStatus(rs.getString("project_status"));

                students.add(student);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching students with project details:");
            e.printStackTrace();
        }

        return students;
    }



  public List<User> getAllUsersWithProjects() { 
    List<User> users = new ArrayList<>();
    
    String sql = "SELECT u.id, u.full_name, u.email, p.title AS project_title, " +
                 "p.supervisor_name, p.status AS project_status " + // Corrected supervisor_name and project_status 
                 "FROM users u " +
                 "LEFT JOIN student_projects sp ON u.id = sp.student_id " +
                 "LEFT JOIN projects p ON sp.project_id = p.id " +
                 "WHERE u.role = 'STUDENT'";  // Removed 'supervisors' table and used supervisor_name directly from projects

    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setFullName(rs.getString("full_name"));
            user.setEmail(rs.getString("email"));
            user.setProjectName(rs.getString("project_title"));
            user.setSupervisorName(rs.getString("supervisor_name"));  // Getting supervisor name directly
            user.setStatus(rs.getString("project_status"));  // Getting project status directly

            users.add(user);
        }

    } catch (SQLException e) {
        System.err.println("Error fetching users with projects:");
        e.printStackTrace();
    }

    return users;
}

public int getActiveSupervisors() {
    String sql = "SELECT COUNT(*) FROM users WHERE role = 'SUPERVISOR'";
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            return rs.getInt(1);  // Return the count of active supervisors
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;  // Return 0 if there's an error or no data
}
public int getActiveStudents() {
    String sql = "SELECT COUNT(*) FROM users WHERE role = 'STUDENT'";
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            return rs.getInt(1);  // Return the count of active students
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;  // Return 0 if there's an error or no data
}
public int getTotalUsers() {
    String sql = "SELECT COUNT(*) FROM users";
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            return rs.getInt(1);  // Return the count of total users
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;  // Return 0 if there's an error or no data
}






}
