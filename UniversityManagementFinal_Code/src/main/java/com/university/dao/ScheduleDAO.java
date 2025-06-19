package com.university.dao;
import com.university.model.Schedule;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List; 
import com.university.util.DatabaseConnection;
public class ScheduleDAO {
    public void addSchedule(Schedule schedule) {
        try (Connection conn = DatabaseConnection.fetchConnection()) {
            String sql = "INSERT INTO schedule (project_id, task_name, start_date, end_date) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, schedule.getProjectId());
            ps.setString(2, schedule.getTaskName());
            ps.setDate(3, new java.sql.Date(schedule.getStartDate().getTime()));
            ps.setDate(4, new java.sql.Date(schedule.getEndDate().getTime()));

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Schedule> getSchedulesByProject(int projectId) {
        List<Schedule> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.fetchConnection()) {
            String sql = "SELECT * FROM schedule WHERE project_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, projectId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Schedule s = new Schedule(
                    rs.getInt("project_id"),
                    rs.getString("task_name"),
                    rs.getDate("start_date"),
                    rs.getDate("end_date")
                );
                s.setScheduleId(rs.getInt("schedule_id"));
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    
}

