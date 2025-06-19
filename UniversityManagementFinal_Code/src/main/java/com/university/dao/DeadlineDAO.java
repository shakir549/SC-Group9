package com.university.dao;

import com.university.model.Deadline;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.university.util.DatabaseConnection;

public class DeadlineDAO {

    // This method will save a deadline to the database
    public boolean addDeadline(Deadline deadline) {
        String sql = "INSERT INTO deadlines (project_id, deadline_date, description) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.fetchConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deadline.getProjectId());        // Set the project_id field
            ps.setDate(2, new java.sql.Date(deadline.getDeadlineDate().getTime()));  // Set the deadline_date field
            ps.setString(3, deadline.getDescription());   // Set the description field

            int rowsAffected = ps.executeUpdate();  // Execute the SQL statement
            return rowsAffected > 0;  // Return true if a row was inserted
        } catch (Exception e) {
            e.printStackTrace();
            return false;  // Return false if an error occurs
        }
    }

    // Fetches deadlines by project ID
    public List<Deadline> getDeadlinesByProject(int projectId) {
        List<Deadline> deadlines = new ArrayList<>();
        String sql = "SELECT * FROM deadlines WHERE project_id = ?";
        
        try (Connection conn = DatabaseConnection.fetchConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, projectId);  // Set project ID in the query
            
            // Execute the query and retrieve results
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                // Create a new Deadline object for each row
                Deadline deadline = new Deadline(
                        rs.getInt("project_id"),
                        rs.getDate("deadline_date"),
                        rs.getString("description")
                );
                deadlines.add(deadline);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return deadlines;
    }
}
