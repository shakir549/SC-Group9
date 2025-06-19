package com.university.dao;

import com.university.model.ProjectVersion;
import com.university.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectVersionDAO {

    // Method to get project versions by submission ID
   public List<ProjectVersion> getVersionsBySubmission(int submissionId) {
    List<ProjectVersion> versions = new ArrayList<>();
    String sql = "SELECT * FROM project_versions WHERE submission_id = ?";

    try (Connection conn = DatabaseConnection.fetchConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, submissionId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ProjectVersion version = new ProjectVersion(
                rs.getInt("version_number"),
                rs.getString("file_path"),
                rs.getTimestamp("upload_date"),
                rs.getInt("submission_id")
            );
            versions.add(version);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return versions;
}

}
