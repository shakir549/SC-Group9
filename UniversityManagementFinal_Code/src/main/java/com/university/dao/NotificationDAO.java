package com.university.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.university.model.Notification;
import com.university.util.DatabaseConnection;

public class NotificationDAO {

    // Adds a new notification to the database
  public void sendNotification(Notification note) {
    String sql = "INSERT INTO notifications (sender_id, recipient_id, subject, message, send_date, is_read, importance) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, note.getSenderId());  // senderId from the session
        ps.setInt(2, note.getRecipientId()); // recipientId passed as method parameter
        ps.setString(3, note.getSubject()); // subject passed as the method parameter
        ps.setString(4, note.getMessage()); // message passed as the method parameter
        ps.setTimestamp(5, note.getSendDate()); // send date
        ps.setString(6, note.getImportance()); // importance
        ps.setString(7, note.getIsRead()); // read status

        int rowsAffected = ps.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Notification sent successfully!");
        } else {
            System.out.println("Failed to send notification.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



    // Retrieves a list of notifications for a user by their user ID
    public List<Notification> getUserNotifications(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE recipient_id = ? ORDER BY send_date DESC";

        try (Connection conn = DatabaseConnection.fetchConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set the user ID parameter for the query
            ps.setInt(1, userId);

            // Execute the query and get the result set
            ResultSet rs = ps.executeQuery();

            // Iterate through the result set and create Notification objects
            while (rs.next()) {
                Notification n = new Notification(
                        rs.getInt("sender_id"),
                        rs.getInt("recipient_id"),
                        rs.getString("subject"),
                        rs.getString("message"),
                        rs.getTimestamp("send_date"),
                        rs.getString("is_read"),
                        rs.getString("importance")
                );
                n.setNotificationId(rs.getInt("notification_id"));
                notifications.add(n);
            }

        } catch (Exception e) {
            System.err.println("Error while fetching notifications for user ID " + userId + ":");
            e.printStackTrace();
        }
        return notifications;
    }


    public List<Notification> getSentNotifications(int senderId) {
    List<Notification> notifications = new ArrayList<>();
    String sql = "SELECT n.send_date AS date, n.subject, n.message AS recipients, n.importance " +
                 "FROM notifications n WHERE n.sender_id = ? ORDER BY n.send_date DESC";

    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, senderId);  // Set the sender's ID (admin's ID)
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Notification notification = new Notification();
            notification.setSendDate(rs.getTimestamp("date"));
            notification.setSubject(rs.getString("subject"));
            notification.setMessage(rs.getString("recipients"));
            notification.setImportance(rs.getString("importance"));
            notifications.add(notification);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return notifications;
}

}
