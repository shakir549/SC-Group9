package com.university.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import com.university.controller.UserController;
import com.university.model.ProjectVersion;

public class VersionViewer extends JFrame {
    public VersionViewer(int submissionId) {
        setTitle("Project Versions");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columns = {"Version", "File Path", "Upload Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        List<ProjectVersion> versions = UserController.getInstance().getProjectVersions(submissionId);
        for (ProjectVersion v : versions) {
            model.addRow(new Object[]{v.getVersionNumber(), v.getFilePath(), v.getUploadDate()});
        }

        JTable table = new JTable(model);
        add(new JScrollPane(table));
        setVisible(true);
    }
}
