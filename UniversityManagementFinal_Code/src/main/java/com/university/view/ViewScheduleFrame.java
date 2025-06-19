package com.university.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import com.university.model.Schedule;
import com.university.controller.UserController;

public class ViewScheduleFrame extends JFrame {
    public ViewScheduleFrame(int projectId) {
        setTitle("View Project Schedule");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columns = {"Task", "Start Date", "End Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        List<Schedule> schedules = UserController.getInstance().getSchedulesByProject(projectId);
        for (Schedule s : schedules) {
            model.addRow(new Object[] {
                s.getTaskName(), 
                s.getStartDate(), 
                s.getEndDate()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
}