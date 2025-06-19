package com.university.dao;

import com.university.model.Activity;
import com.university.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityDAO {

    public List<Activity> getRecentActivity() {
        List<Activity> activityList = new ArrayList<>();
        String sql = "SELECT u.full_name AS user_name, a.action, a.time " +
                     "FROM activity_log a " +
                     "JOIN users u ON a.user_id = u.id " +
                     "WHERE u.role IN ('STUDENT', 'SUPERVISOR', 'ADMIN') " +
                     "ORDER BY a.time DESC " +
                     "LIMIT 10";
        
        try (Connection conn = DatabaseConnection.fetchConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Activity activity = new Activity();
                activity.setUserName(rs.getString("user_name"));
                activity.setAction(rs.getString("action"));
                activity.setTime(rs.getTimestamp("time"));
                
                activityList.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return activityList;
    }
}
