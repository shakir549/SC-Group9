package com.university.dao;

import com.university.model.Project;
import com.university.model.Submission;
import com.university.model.User;
import com.university.util.DatabaseConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubmissionDAO {

    private static final Logger LOGGER = Logger.getLogger(SubmissionDAO.class.getName());

    // Insert new submission with file
    public boolean insertSubmission(Submission submission, File file) {
    // Validation checks for submission and file
    if (!isValidSubmission(submission) || !isValidFile(file)) return false;

    // Define the SQL query for inserting a new submission into the database
    String sql = "INSERT INTO submissions (project_id, student_id, file_path, description, version, status, file_data) "
               + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    // Use try-with-resources to automatically manage resources like the connection and prepared statement
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);  // Get generated keys
         FileInputStream fis = new FileInputStream(file)) {

        conn.setAutoCommit(false);  // Disable auto-commit for transaction management

        // Set the values for the SQL query parameters
        stmt.setInt(1, submission.getProject().getId());  // Set project ID
        stmt.setInt(2, submission.getStudent().getId());  // Set student ID
        stmt.setString(3, submission.getFilePath() != null ? submission.getFilePath() : file.getAbsolutePath());  // Set file path
        stmt.setString(4, submission.getDescription());  // Set description
        stmt.setInt(5, submission.getVersion());  // Set version (default 1 if not provided)
        stmt.setString(6, submission.getStatus().name());  // Set status as string ('PENDING', 'APPROVED', 'REJECTED')
        stmt.setBinaryStream(7, fis, (int) file.length());  // Set file data as binary stream

        // Execute the SQL statement to insert the submission data
        int rows = stmt.executeUpdate();
        if (rows == 0) {
            conn.rollback();  // Rollback if no rows were inserted
            return false;
        }

        // Retrieve the generated submission ID (auto-incremented)
        try (ResultSet keys = stmt.getGeneratedKeys()) {
            if (keys.next()) {
                submission.setId(keys.getInt(1));  // Set the generated ID for the submission object
            }
        }

        conn.commit();  // Commit the transaction
        return true;  // Return true indicating successful insertion

    } catch (SQLException | IOException e) {
        e.printStackTrace();  // Print any SQL or I/O exceptions
        return false;  // Return false if an exception occurred
    }
}

    // Update existing submission with file
    public boolean updateSubmission(Submission submission, File file) {
        if (!isValidSubmission(submission) || !isValidFile(file)) return false;

        String sql = "UPDATE submissions SET project_id = ?, student_id = ?, file_path = ?, description = ?, version = ?, status = ?, file_data = ? "
                   + "WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();  // Get connection here
             PreparedStatement stmt = conn.prepareStatement(sql);
             FileInputStream fis = new FileInputStream(file)) {

            conn.setAutoCommit(false);

            stmt.setInt(1, submission.getProject().getId());
            stmt.setInt(2, submission.getStudent().getId());
            stmt.setString(3, submission.getFilePath() != null ? submission.getFilePath() : file.getAbsolutePath());
            stmt.setString(4, submission.getDescription());
            stmt.setInt(5, submission.getVersion());
            stmt.setString(6, submission.getStatus().name());
            stmt.setBinaryStream(7, fis, (int) file.length());
            stmt.setInt(8, submission.getId());

            boolean success = stmt.executeUpdate() > 0;
            if (success) {
                conn.commit();
                LOGGER.info("✅ Submission updated: ID " + submission.getId());
            } else {
                conn.rollback();
                LOGGER.warning("⚠️ No rows updated.");
            }
            return success;

        } catch (SQLException | IOException e) {
            LOGGER.log(Level.SEVERE, "❌ Failed to update submission.", e);
            return false;
        }
    }

    // Retrieve file data
    public byte[] getSubmissionFileData(int submissionId) {
        String sql = "SELECT file_data FROM submissions WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();  // Get connection here
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, submissionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBytes("file_data");
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Failed to retrieve file data.", e);
        }
        return null;
    }

    // Fetch submission by ID
    public Submission getSubmissionById(int id) {
        String sql = "SELECT * FROM submissions WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();  // Get connection here
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractSubmissionFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Failed to retrieve submission by ID.", e);
        }
        return null;
    }

    // Fetch all submissions for a project
    public List<Submission> getSubmissionsByProjectId(int projectId) {
        List<Submission> submissions = new ArrayList<>();
        String sql = "SELECT * FROM submissions WHERE project_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();  // Get connection here
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projectId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    submissions.add(extractSubmissionFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Failed to get submissions by project ID.", e);
        }

        return submissions;
    }

    // Fetch all submissions by a student
    public List<Submission> getSubmissionsByStudentId(int studentId) {
        List<Submission> submissions = new ArrayList<>();
        String sql = "SELECT * FROM submissions WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();  // Get connection here
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    submissions.add(extractSubmissionFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Failed to get submissions by student ID.", e);
        }

        return submissions;
    }

    // Extract from ResultSet
    private Submission extractSubmissionFromResultSet(ResultSet rs) throws SQLException {
        Submission s = new Submission();
        s.setId(rs.getInt("id"));

        // Get Project from ResultSet
        Project project = new Project();
        project.setId(rs.getInt("project_id"));
        s.setProject(project);

        // Get Student from ResultSet
        User student = new User();
        student.setId(rs.getInt("student_id"));
        s.setStudent(student);

        // Get the other fields
        s.setFilePath(rs.getString("file_path"));
        s.setDescription(rs.getString("description"));

        // Convert Timestamp to LocalDateTime
        Timestamp timestamp = rs.getTimestamp("submission_date");
        if (timestamp != null) {
            s.setSubmissionDate(timestamp.toLocalDateTime());  // Convert Timestamp to LocalDateTime
        }

        s.setVersion(rs.getInt("version"));
        s.setStatus(Submission.Status.valueOf(rs.getString("status")));
        s.setFileData(rs.getBytes("file_data"));

        return s;
    }

    // Helper validation
    private boolean isValidSubmission(Submission s) {
        if (s == null || s.getProject() == null || s.getStudent() == null) {
            LOGGER.severe("❌ Invalid Submission or related entities are null.");
            return false;
        }
        return true;
    }

    private boolean isValidFile(File file) {
        if (file == null || !file.exists() || file.length() == 0) {
            LOGGER.severe("❌ File is null, missing, or empty.");
            return false;
        }
        return true;
    }
}
