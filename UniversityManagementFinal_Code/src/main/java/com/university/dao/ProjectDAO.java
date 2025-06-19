package com.university.dao;

import com.university.model.Project;

import java.sql.*;
import com.university.util.DatabaseConnection;

public class ProjectDAO {
  
    // Method to add a project to the database
public boolean addProject(Project project) {
    try (Connection conn = DatabaseConnection.fetchConnection()) {
        // Log values for debugging purposes
        System.out.println("Supervisor: " + project.getSupervisorName());
        System.out.println("Students: " + String.join(",", project.getStudentNames()));
        
        // SQL query to insert the project into the database
        String sql = "INSERT INTO projects (title, description, supervisor_name, student_names, deadline, status) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        
        // Set the parameters for the SQL query
        ps.setString(1, project.getTitle());
        ps.setString(2, project.getDescription());
        ps.setString(3, project.getSupervisorName()); // Store supervisor's name correctly
        ps.setString(4, String.join(",", project.getStudentNames())); // Ensure student names are correctly joined
        ps.setDate(5, new java.sql.Date(project.getDeadline().getTime())); // Convert Date to java.sql.Date
        ps.setString(6, project.getStatus()); // Store the status of the project
        
        // Execute the update and check if the project was added successfully
        int result = ps.executeUpdate();
        return result > 0;  // Return true if the project was added successfully
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

public int getProjectsInProgressCount() {
    String sql = "SELECT COUNT(*) FROM projects WHERE status = 'In Progress'";
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            return rs.getInt(1);  // Return the count of "In Progress" projects
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;  // Return 0 if no projects found
}




}
