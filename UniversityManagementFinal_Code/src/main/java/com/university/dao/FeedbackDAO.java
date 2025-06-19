package com.university.dao;

import com.university.model.Feedback;
import com.university.model.Submission;
import com.university.model.User;
import com.university.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class to handle CRUD operations for Feedback entities.
 */
public class FeedbackDAO {

    public Feedback getFeedbackById(int id) {
        String sql = "SELECT * FROM feedback WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractFeedbackFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Feedback getFeedbackBySubmission(int submissionId) {
        String sql = "SELECT * FROM feedback WHERE submission_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, submissionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractFeedbackFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Feedback> getFeedbackBySupervisor(int supervisorId) {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "SELECT * FROM feedback WHERE supervisor_id = ? ORDER BY feedback_date DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, supervisorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    feedbackList.add(extractFeedbackFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbackList;
    }

    public List<Feedback> getFeedbackForStudent(int studentId) {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "SELECT f.* FROM feedback f " +
                     "JOIN submissions s ON f.submission_id = s.id " +
                     "WHERE s.student_id = ? ORDER BY f.feedback_date DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    feedbackList.add(extractFeedbackFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbackList;
    }

    public List<Feedback> getFeedbackByProject(int projectId) {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "SELECT f.* FROM feedback f " +
                     "JOIN submissions s ON f.submission_id = s.id " +
                     "WHERE s.project_id = ? ORDER BY f.feedback_date DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, projectId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    feedbackList.add(extractFeedbackFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbackList;
    }

    public boolean insertFeedback(Feedback feedback) {
        String sql = "INSERT INTO feedback (submission_id, supervisor_id, comments, grade, feedback_date) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, feedback.getSubmission().getId());
            stmt.setInt(2, feedback.getSupervisor().getId());
            stmt.setString(3, feedback.getComments());
            stmt.setInt(4, feedback.getGrade());
            stmt.setTimestamp(5, new Timestamp(feedback.getFeedbackDate().getTime()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) return false;

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    feedback.setId(generatedKeys.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateFeedback(Feedback feedback) {
        String sql = "UPDATE feedback SET submission_id = ?, supervisor_id = ?, comments = ?, grade = ?, feedback_date = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, feedback.getSubmission().getId());
            stmt.setInt(2, feedback.getSupervisor().getId());
            stmt.setString(3, feedback.getComments());
            stmt.setInt(4, feedback.getGrade());
            stmt.setTimestamp(5, new Timestamp(feedback.getFeedbackDate().getTime()));
            stmt.setInt(6, feedback.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteFeedback(int id) {
        String sql = "DELETE FROM feedback WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public double getAverageGradeForProject(int projectId) {
        String sql = "SELECT AVG(f.grade) AS avg_grade FROM feedback f " +
                     "JOIN submissions s ON f.submission_id = s.id " +
                     "WHERE s.project_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, projectId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("avg_grade");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public double getAverageGradeForStudent(int studentId) {
        String sql = "SELECT AVG(f.grade) AS avg_grade FROM feedback f " +
                     "JOIN submissions s ON f.submission_id = s.id " +
                     "WHERE s.student_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("avg_grade");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private Feedback extractFeedbackFromResultSet(ResultSet rs) throws SQLException {
        Feedback feedback = new Feedback();
        feedback.setId(rs.getInt("id"));

        
        Submission submission = new Submission();
        submission.setId(rs.getInt("submission_id"));
        feedback.setSubmission(submission);

        User supervisor = new User();
        supervisor.setId(rs.getInt("supervisor_id"));
        feedback.setSupervisor(supervisor);

        feedback.setComments(rs.getString("comments"));
        feedback.setGrade(rs.getInt("grade"));
        feedback.setFeedbackDate(rs.getTimestamp("feedback_date"));

        return feedback;
    }
}
