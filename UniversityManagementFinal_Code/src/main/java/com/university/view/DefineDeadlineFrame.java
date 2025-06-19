package com.university.view;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import com.university.controller.UserController;
import com.university.model.Deadline;

public class DefineDeadlineFrame extends JFrame {

    public DefineDeadlineFrame(int projectId) {
        setTitle("Define Project Deadline");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set up the layout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding for components
        gbc.anchor = GridBagConstraints.WEST;

        // Date label and text field
        JLabel dateLabel = new JLabel("Deadline (yyyy-MM-dd):");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(dateLabel, gbc);

        JTextField dateField = new JTextField(10); // Set column width for better layout
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(dateField, gbc);

        // Description label and text field
        JLabel descLabel = new JLabel("Description:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(descLabel, gbc);

        JTextArea descField = new JTextArea(3, 20); // Using JTextArea for multiline input
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JScrollPane(descField), gbc); // Adding JScrollPane for better handling

        // Submit button
        JButton submitButton = new JButton("Set Deadline");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(submitButton, gbc);

        // Submit button action listener
       submitButton.addActionListener(e -> {
    // Validate fields before processing
    String dateText = dateField.getText().trim();
    String description = descField.getText().trim();

    if (dateText.isEmpty() || description.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Both fields are required", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        // Parse the deadline date
        Date deadlineDate = Date.valueOf(dateText);

        // Create the Deadline object with an additional deadlineId (e.g., set to 0 or another value)
      
        Deadline deadline = new Deadline( projectId, deadlineDate, description);

        // Call UserController to save the deadline
        UserController.getInstance().defineDeadline(deadline);

        // Show success message
        JOptionPane.showMessageDialog(this, "Deadline set successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Clear the fields
        dateField.setText("");
        descField.setText("");
        dispose(); // Close the frame

    } catch (IllegalArgumentException ex) {
        // Catch invalid date format
        JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
    }
});

        // Add components to the frame
        add(panel);
        setVisible(true);
    }
}
