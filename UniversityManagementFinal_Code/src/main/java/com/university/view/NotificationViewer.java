package com.university.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.List;
import com.university.controller.UserController;
import com.university.model.Notification;

public class NotificationViewer extends JFrame {

    public NotificationViewer(int userId) {
        setTitle("My Notifications");
        setSize(600, 400);
        setLocationRelativeTo(null); // Centers the window on the screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Table columns definition
        String[] columns = {"Message", "Status", "Timestamp"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        // Fetch notifications using the UserController
        List<Notification> notes = UserController.getInstance().getNotifications(userId);
        
        // Loop through notifications and add them to the table model
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); // Formatting timestamp
        for (Notification n : notes) {
            String formattedDate = sdf.format(n.getSendDate()); // Format the timestamp
            model.addRow(new Object[]{n.getMessage(), n.getIsRead(), formattedDate});
        }

        // Create JTable with the model
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // Make sure the columns auto resize
        table.setFillsViewportHeight(true); // Ensure the table takes up all available space

        // Add the table inside a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);

        // Display the frame
        setVisible(true);
    }
}
