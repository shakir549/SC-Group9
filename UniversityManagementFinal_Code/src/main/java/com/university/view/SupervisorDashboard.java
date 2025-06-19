package com.university.view;

import com.university.controller.UserController;
import com.university.dao.ProjectDAO;
import com.university.model.Project;
import com.university.model.Role;
import com.university.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Dashboard for supervisor users.
 * Provides access to supervisor-specific functionalities.
 * Features responsive design to adapt to different screen sizes.
 */
public class SupervisorDashboard extends JFrame {
    private final User currentUser;
    private final JPanel contentPanel;
    private final JPanel sidebarPanel;
    private final JPanel mainPanel;
    
    // Minimum dimensions for the dashboard
    private static final int MIN_WIDTH = 800;
    private static final int MIN_HEIGHT = 600;
    
    // Sidebar width variables
    private static final int SIDEBAR_EXPANDED_WIDTH = 200;
    private static final int SIDEBAR_COLLAPSED_WIDTH = 60;
    private boolean sidebarCollapsed = false;
    
    public SupervisorDashboard(User user) {
        this.currentUser = user;
        
        // Set up the frame
        setTitle("Supervisor Dashboard - " + user.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setSize(1024, 768); // Larger initial size
        setLocationRelativeTo(null); // Center on screen
        
        // Create the main panel with border layout
        mainPanel = new JPanel(new BorderLayout(5, 5));
        
        // Create the header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create the sidebar panel
        sidebarPanel = createSidebarPanel();
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        
        // Create the content panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new CardLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add different content panels
        JPanel welcomePanel = createWelcomePanel();
        JPanel assignProjectPanel = createAssignProjectPanel();
        JPanel reviewWorkPanel = createReviewWorkPanel();
        JPanel provideFeedbackPanel = createProvideFeedbackPanel();
        JPanel approveRejectPanel = createApproveRejectPanel();
        JPanel gradePanel = createGradePanel();
        
        contentPanel.add(welcomePanel, "welcome");
        contentPanel.add(assignProjectPanel, "assignProject");
        contentPanel.add(reviewWorkPanel, "reviewWork");
        contentPanel.add(provideFeedbackPanel, "provideFeedback");
        contentPanel.add(approveRejectPanel, "approveReject");
        contentPanel.add(gradePanel, "grade");
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add a status bar at the bottom
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.setPreferredSize(new Dimension(getWidth(), 25));
        JLabel statusLabel = new JLabel(" Ready");
        statusBar.add(statusLabel, BorderLayout.WEST);
        
        // Add current date to status bar
        JLabel dateLabel = new JLabel(new java.util.Date().toString() + " ");
        statusBar.add(dateLabel, BorderLayout.EAST);
        
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        
        // Add the main panel to the frame
        getContentPane().add(mainPanel);
        
        // Add window resize listener to adjust layout
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustUIForScreenSize();
            }
        });
        
        // Show welcome panel initially
        CardLayout cl = (CardLayout) (contentPanel.getLayout());
        cl.show(contentPanel, "welcome");
    }
    
    /**
     * Adjusts UI components based on the current window size
     */
    private void adjustUIForScreenSize() {
        int width = getWidth();
        
        // Auto-collapse sidebar on small screens
        if (width < 600 && !sidebarCollapsed) {
            collapseSidebar();
        } else if (width >= 600 && sidebarCollapsed) {
            expandSidebar();
        }
        
        // Update fonts and component sizes
        updateFontSizes();
        
        revalidate();
        repaint();
    }
    
    /**
     * Updates font sizes based on screen size
     */
    private void updateFontSizes() {
        int width = getWidth();
        
        Font headerFont;
        Font regularFont;
        Font smallFont;
        
        if (width < 600) {
            headerFont = new Font("Arial", Font.BOLD, 14);
            regularFont = new Font("Arial", Font.PLAIN, 12);
            smallFont = new Font("Arial", Font.PLAIN, 10);
        } else if (width < 900) {
            headerFont = new Font("Arial", Font.BOLD, 16);
            regularFont = new Font("Arial", Font.PLAIN, 14);
            smallFont = new Font("Arial", Font.PLAIN, 12);
        } else {
            headerFont = new Font("Arial", Font.BOLD, 18);
            regularFont = new Font("Arial", Font.PLAIN, 15);
            smallFont = new Font("Arial", Font.PLAIN, 13);
        }
        
        // Update component fonts throughout the UI
        updateComponentFonts(this, headerFont, regularFont, smallFont);
    }
    
    /**
     * Recursively updates fonts for component tree
     */
    private void updateComponentFonts(Component comp, Font headerFont, Font regularFont, Font smallFont) {
        if (comp instanceof JLabel) {
            JLabel label = (JLabel) comp;
            if (label.getFont().isBold()) {
                label.setFont(headerFont);
            } else {
                label.setFont(regularFont);
            }
        } else if (comp instanceof JButton) {
            comp.setFont(regularFont);
        } else if (comp instanceof JTextField || comp instanceof JTextArea) {
            comp.setFont(regularFont);
        } else if (comp instanceof JTable) {
            JTable table = (JTable) comp;
            table.setFont(regularFont);
            table.getTableHeader().setFont(smallFont);
        } else if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                updateComponentFonts(child, headerFont, regularFont, smallFont);
            }
        }
    }
    
    /**
     * Collapses the sidebar to icon-only mode
     */
    private void collapseSidebar() {
        sidebarPanel.setPreferredSize(new Dimension(SIDEBAR_COLLAPSED_WIDTH, getHeight()));
        sidebarCollapsed = true;
        
        // Hide text on buttons, show only icons
        for (Component comp : sidebarPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.setText("");
            }
        }
        
        revalidate();
    }
    
    /**
     * Expands the sidebar to show text and icons
     */
    private void expandSidebar() {
        sidebarPanel.setPreferredSize(new Dimension(SIDEBAR_EXPANDED_WIDTH, getHeight()));
        sidebarCollapsed = false;
        
        // Restore button text
        Component[] components = sidebarPanel.getComponents();
        String[] buttonTexts = {"Dashboard", "Assign Project", "Review Work", 
                               "Provide Feedback", "Approve/Reject", "Grade"};
        
        for (int i = 0; i < components.length && i < buttonTexts.length; i++) {
            if (components[i] instanceof JButton) {
                ((JButton) components[i]).setText(buttonTexts[i]);
            }
        }
        
        revalidate();
    }
    
    /**
     * Creates the header panel with user info and logout button.
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        // User info label with icon
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        userInfoPanel.setOpaque(false);
        
        // Add user icon
        ImageIcon userIcon = createDefaultUserIcon();
        JLabel iconLabel = new JLabel(userIcon);
        userInfoPanel.add(iconLabel);
        
        // User info text
        JLabel userInfoLabel = new JLabel("Logged in as: " + currentUser.getFullName() + " (Supervisor)");
        userInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userInfoPanel.add(userInfoLabel);
        
        panel.add(userInfoPanel, BorderLayout.WEST);
        
        // Right side panel with buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        
        // Projects button
        JButton projectsButton = new JButton("Projects");
        projectsButton.setFocusPainted(false);
        rightPanel.add(projectsButton);
        
        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        rightPanel.add(logoutButton);
        
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Creates a default user icon
     */
    private ImageIcon createDefaultUserIcon() {
        // Create a simple colored circle as the user icon
        int size = 24;
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(0, 128, 128)); // Teal color for supervisor
        g2d.fillOval(0, 0, size, size);
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    /**
     * Creates the sidebar panel with navigation buttons.
     */
    private JPanel createSidebarPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 0, 10));
        panel.setBackground(new Color(230, 230, 230));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
        panel.setPreferredSize(new Dimension(SIDEBAR_EXPANDED_WIDTH, getHeight()));
        
        // Create navigation buttons
        JButton dashboardButton = createNavButton("Dashboard", "welcome");
        JButton assignProjectButton = createNavButton("Assign Project", "assignProject");
        JButton reviewWorkButton = createNavButton("Review Work", "reviewWork");
        JButton provideFeedbackButton = createNavButton("Provide Feedback", "provideFeedback");
        JButton approveRejectButton = createNavButton("Approve/Reject", "approveReject");
        JButton gradeButton = createNavButton("Grade", "grade");
        
        // Add buttons to panel
        panel.add(dashboardButton);
        panel.add(assignProjectButton);
        panel.add(reviewWorkButton);
        panel.add(provideFeedbackButton);
        panel.add(approveRejectButton);
        panel.add(gradeButton);
        
        return panel;
    }
    
    /**
     * Creates a navigation button with the given label and action.
     */
    private JButton createNavButton(String label, String panelName) {
        JButton button = new JButton(label);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (contentPanel.getLayout());
                cl.show(contentPanel, panelName);
            }
        });
        return button;
    }
    
    /**
     * Creates the welcome panel with user information.
     */
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(welcomeLabel, BorderLayout.NORTH);
        
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        infoPanel.add(new JLabel("Username: " + currentUser.getUsername()));
        infoPanel.add(new JLabel("Email: " + currentUser.getEmail()));
        infoPanel.add(new JLabel("Role: Supervisor"));
        
        // Add a description of available features
        JTextArea descriptionArea = new JTextArea(
            "As a supervisor, you can:\n\n" +
            "• Assign projects to students\n" +
            "• Review student work\n" +
            "• Provide feedback on submissions\n" +
            "• Approve or reject student submissions\n" +
            "• Grade completed projects"
        );
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        infoPanel.add(descriptionArea);
        panel.add(infoPanel, BorderLayout.CENTER);
        
        // Add summary of supervised projects
        JPanel projectsPanel = new JPanel(new BorderLayout());
        projectsPanel.setBorder(BorderFactory.createTitledBorder("Supervised Projects"));
        
        // Create a table to display project summary
        String[] columnNames = {"Project", "Student", "Status", "Deadline"};
        Object[][] data = {
            {"Final Year Project", "inam Student", "In Progress", "2023-06-30"},
            {"Research Paper", "afrab Student", "Completed", "2023-04-30"},
            {"Group Assignment", "shakir Student", "Rejected", "2023-03-31"}
        };
        
        JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(500, 100));
        table.setFillsViewportHeight(true);
        
        JScrollPane scrollPane = new JScrollPane(table);
        projectsPanel.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(projectsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Creates the assign project panel.
     */
    private JPanel createAssignProjectPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JLabel titleLabel = new JLabel("Assign Project");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    panel.add(titleLabel, BorderLayout.NORTH);

    JPanel formPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.anchor = GridBagConstraints.WEST;

    // Project title
    JLabel titleFormLabel = new JLabel("Project Title:");
    gbc.gridx = 0;
    gbc.gridy = 0;
    formPanel.add(titleFormLabel, gbc);

    JTextField titleField = new JTextField(30);
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    formPanel.add(titleField, gbc);

    // Description
    JLabel descriptionLabel = new JLabel("Description:");
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.NONE;
    formPanel.add(descriptionLabel, gbc);

    JTextArea descriptionArea = new JTextArea(5, 30);
    descriptionArea.setLineWrap(true);
    descriptionArea.setWrapStyleWord(true);
    JScrollPane scrollPane = new JScrollPane(descriptionArea);

    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.BOTH;
    formPanel.add(scrollPane, gbc);

    // Student selection
    JLabel studentLabel = new JLabel("Assign to Student:");
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.fill = GridBagConstraints.NONE;
    formPanel.add(studentLabel, gbc);

    UserController userController = UserController.getInstance();
    List<User> students = userController.getUsersByRole(Role.STUDENT);

    String[] studentNames = new String[students.size()];
    for (int i = 0; i < students.size(); i++) {
        studentNames[i] = students.get(i).getFullName();
    }

    JComboBox<String> studentComboBox = new JComboBox<>(studentNames.length > 0 ? studentNames : new String[]{"inam Student"});
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    formPanel.add(studentComboBox, gbc);

    // Deadline
    JLabel deadlineLabel = new JLabel("Deadline:");
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.fill = GridBagConstraints.NONE;
    formPanel.add(deadlineLabel, gbc);

    JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    JComboBox<String> dayComboBox = new JComboBox<>();
    for (int i = 1; i <= 31; i++) {
        dayComboBox.addItem(String.valueOf(i));
    }

    JComboBox<String> monthComboBox = new JComboBox<>(new String[]{
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    });

    JComboBox<String> yearComboBox = new JComboBox<>();
    int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
    for (int i = currentYear; i <= currentYear + 5; i++) {
        yearComboBox.addItem(String.valueOf(i));
    }

    datePanel.add(dayComboBox);
    datePanel.add(new JLabel("/"));
    datePanel.add(monthComboBox);
    datePanel.add(new JLabel("/"));
    datePanel.add(yearComboBox);

    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    formPanel.add(datePanel, gbc);

    // Assign button
    JButton assignButton = new JButton("Assign Project");
    assignButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (titleField.getText().isEmpty() || descriptionArea.getText().isEmpty()) {
                JOptionPane.showMessageDialog(SupervisorDashboard.this, 
                    "Please fill in all required fields", 
                    "Assignment Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get the selected deadline date
            int day = Integer.parseInt(dayComboBox.getSelectedItem().toString());
            int month = monthComboBox.getSelectedIndex() + 1; // 1-based index for month
            int year = Integer.parseInt(yearComboBox.getSelectedItem().toString());

            // Check if the selected deadline is in the future
            java.util.Calendar selectedDeadline = java.util.Calendar.getInstance();
            selectedDeadline.set(year, month - 1, day, 0, 0, 0); // Set the time to midnight

            java.util.Calendar currentDate = java.util.Calendar.getInstance();

            if (selectedDeadline.before(currentDate)) {
                // If the selected date is in the past
                JOptionPane.showMessageDialog(SupervisorDashboard.this, 
                    "The selected deadline is in the past. Please choose a future date.", 
                    "Invalid Deadline", 
                    JOptionPane.ERROR_MESSAGE);
            } else {
                // Create a Project object and save it in the database
             User assignedStudent = students.get(studentComboBox.getSelectedIndex());
// Create a List<String> for student names
    List<String> studentNames = new ArrayList<>();
studentNames.add(assignedStudent.getFullName());  // Add selected student to the list

Project newProject = new Project(
    titleField.getText(),
    descriptionArea.getText(),
    currentUser.getFullName(),  // Supervisor's name
    studentNames,               // List of student names
    selectedDeadline.getTime()  // Deadline
);

                
                ProjectDAO projectDAO = new ProjectDAO();
                if (projectDAO.addProject(newProject)) {
                    JOptionPane.showMessageDialog(SupervisorDashboard.this, 
                        "Project assigned successfully!",
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(SupervisorDashboard.this, 
                        "Failed to assign project", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }

                // Clear form
                titleField.setText("");
                descriptionArea.setText("");
                studentComboBox.setSelectedIndex(0);
                dayComboBox.setSelectedIndex(0);
                monthComboBox.setSelectedIndex(0);
                yearComboBox.setSelectedIndex(0);
            }
        }
    });
    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.EAST;
    formPanel.add(assignButton, gbc);

    panel.add(formPanel, BorderLayout.CENTER);

    return panel;
}
    
    /**
     * Creates the review work panel.
     */
    private JPanel createReviewWorkPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Review Student Work");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Create a table to display submissions
        String[] columnNames = {"Project", "Student", "Version", "Submit Date", "Status"};
        Object[][] data = {
            {"Final Year Project", "inam Student", "1.0", "2023-05-10", "Pending"},
            {"Research Paper", "aftab Student", "2.0", "2023-04-15", "Reviewed"},
            {"Group Assignment", "shakir Student", "1.0", "2023-03-20", "Rejected"}
        };
        
        JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setFillsViewportHeight(true);
        
        JScrollPane tableScrollPane = new JScrollPane(table);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Add a panel for viewing the selected submission
        JPanel viewPanel = new JPanel(new BorderLayout());
        viewPanel.setBorder(BorderFactory.createTitledBorder("Submission Details"));
        
        JTextArea submissionArea = new JTextArea(5, 30);
        submissionArea.setEditable(false);
        submissionArea.setLineWrap(true);
        submissionArea.setWrapStyleWord(true);
        submissionArea.setText("Select a submission to view details");
        
        JScrollPane submissionScrollPane = new JScrollPane(submissionArea);
        viewPanel.add(submissionScrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewButton = new JButton("View Submission");
        JButton downloadButton = new JButton("Download File");
        
        buttonPanel.add(viewButton);
        buttonPanel.add(downloadButton);
        viewPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        panel.add(viewPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String project = (String) table.getValueAt(selectedRow, 0);
                    String student = (String) table.getValueAt(selectedRow, 1);
                    String version = (String) table.getValueAt(selectedRow, 2);
                    
                    submissionArea.setText(
                        "Project: " + project + "\n" +
                        "Student: " + student + "\n" +
                        "Version: " + version + "\n\n" +
                        "This is a placeholder for the submission content. In a real system, " +
                        "this would show the description provided by the student and other details."
                    );
                } else {
                    JOptionPane.showMessageDialog(SupervisorDashboard.this, 
                        "Please select a submission to view", 
                        "Selection Required", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    JOptionPane.showMessageDialog(SupervisorDashboard.this, 
                        "This is a placeholder for the file download functionality.\n" +
                        "In a real system, this would download the file submitted by the student.",
                        "Download", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(SupervisorDashboard.this, 
                        "Please select a submission to download", 
                        "Selection Required", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        return panel;
    }
    
    /**
     * Creates the provide feedback panel.
     */
    private JPanel createProvideFeedbackPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Provide Feedback");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Submission selection
        JLabel submissionLabel = new JLabel("Select Submission:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(submissionLabel, gbc);
        
        JComboBox<String> submissionComboBox = new JComboBox<>(new String[]{
            "Final Year Project - inam Student (v1.0)",
            "Research Paper - aftab Student (v2.0)",
            "Group Assignment - shakir Student (v1.0)"
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(submissionComboBox, gbc);
        
        // Feedback comments
        JLabel commentsLabel = new JLabel("Comments:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(commentsLabel, gbc);
        
        JTextArea commentsArea = new JTextArea(10, 30);
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(commentsArea);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(scrollPane, gbc);
        
        // Grade
        JLabel gradeLabel = new JLabel("Grade (0-100):");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(gradeLabel, gbc);
        
        JSpinner gradeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(gradeSpinner, gbc);
        
        // Submit button
        JButton submitButton = new JButton("Submit Feedback");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (commentsArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(SupervisorDashboard.this, 
                        "Please provide feedback comments", 
                        "Feedback Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                JOptionPane.showMessageDialog(SupervisorDashboard.this, 
                    "Feedback submitted successfully!\n\n" +
                    "Submission: " + submissionComboBox.getSelectedItem() + "\n" +
                    "Grade: " + gradeSpinner.getValue(),
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Clear form
                submissionComboBox.setSelectedIndex(0);
                commentsArea.setText("");
                gradeSpinner.setValue(0);
            }
        });
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(submitButton, gbc);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the approve/reject panel with enhanced search and submission handling.
     */
    private JPanel createApproveRejectPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create header panel with title and search components
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Approve or Reject Submissions");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Search panel with filters
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JLabel searchLabel = new JLabel("Search:");
        searchPanel.add(searchLabel);
        
        JTextField searchField = new JTextField(15);
        searchPanel.add(searchField);
        
        JLabel filterLabel = new JLabel("Filter by project:");
        searchPanel.add(filterLabel);
        
        JComboBox<String> projectFilterCombo = new JComboBox<>(new String[]{
            "All Projects", "Final Year Project", "Research Paper", "Group Assignment"
        });
        searchPanel.add(projectFilterCombo);
        
        JLabel dateFilterLabel = new JLabel("Date range:");
        searchPanel.add(dateFilterLabel);
        
        // Simple date filter using dropdown
        JComboBox<String> dateFilterCombo = new JComboBox<>(new String[]{
            "All Dates", "Last 7 Days", "Last 30 Days", "This Semester"
        });
        searchPanel.add(dateFilterCombo);
        
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);
        
        headerPanel.add(searchPanel, BorderLayout.CENTER);
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Create a table with more detailed submission information
        String[] columnNames = {"Project", "Student", "Version", "Submit Date", "File Type", "File Size"};
        Object[][] data = {
            {"Final Year Project", "inam Student", "1.0", "2023-05-10", "PDF", "2.4 MB"},
            {"Research Paper", "aftab Student", "2.0", "2023-04-15", "DOCX", "1.8 MB"},
            {"Group Assignment", "shakir Student", "1.0", "2023-03-20", "ZIP", "5.2 MB"}
        };
        
        // Create a DefaultTableModel to easily modify table data
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setFillsViewportHeight(true);
        
        // Set column widths for better readability
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);
        
        // Enable sorting
        table.setAutoCreateRowSorter(true);
        
        JScrollPane tableScrollPane = new JScrollPane(table);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Create details panel to show submission information
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Submission Details"));
        
        JTextArea detailsArea = new JTextArea(5, 30);
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setText("Select a submission to view details");
        
        JScrollPane detailsScrollPane = new JScrollPane(detailsArea);
        detailsPanel.add(detailsScrollPane, BorderLayout.CENTER);
        
        // Add buttons for common actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton viewFileButton = new JButton("View File");
        JButton downloadButton = new JButton("Download");
        
        actionPanel.add(viewFileButton);
        actionPanel.add(downloadButton);
        detailsPanel.add(actionPanel, BorderLayout.SOUTH);
        
        // Create main button panel for approve/reject actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewButton = new JButton("View Submission");
        JButton provideFeedbackButton = new JButton("Provide Feedback");
        JButton approveButton = new JButton("Approve");
        JButton rejectButton = new JButton("Reject");
        
        buttonPanel.add(provideFeedbackButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        
        // Combine details and action panels
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(detailsPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        panel.add(southPanel, BorderLayout.SOUTH);
        
        // Implement search functionality
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText().toLowerCase();
                String projectFilter = (String) projectFilterCombo.getSelectedItem();
                String dateFilter = (String) dateFilterCombo.getSelectedItem();
                
                // Clear current table data
                model.setRowCount(0);
                
                // Filter data based on search criteria
                for (Object[] row : data) {
                    String project = row[0].toString();
                    String student = row[1].toString().toLowerCase();
                    String date = row[3].toString();
                    
                    boolean matchesSearch = searchTerm.isEmpty() || 
                                           student.contains(searchTerm) || 
                                           project.toLowerCase().contains(searchTerm);
                    
                    boolean matchesProject = projectFilter.equals("All Projects") || 
                                             project.equals(projectFilter);
                    
                    boolean matchesDate = true; // By default match all dates
                    
                    // Simple date filtering (in a real app, you'd use proper date parsing)
                    if (dateFilter.equals("Last 7 Days")) {
                        // For demo purposes, assume dates after May 5th are within last 7 days
                        matchesDate = date.compareTo("2023-05-05") > 0;
                    } else if (dateFilter.equals("Last 30 Days")) {
                        // For demo purposes, assume dates after April 15th are within last 30 days
                        matchesDate = date.compareTo("2023-04-15") >= 0;
                    } else if (dateFilter.equals("This Semester")) {
                        // For demo purposes, assume dates after March 1st are this semester
                        matchesDate = date.compareTo("2023-03-01") >= 0;
                    }
                    
                    if (matchesSearch && matchesProject && matchesDate) {
                        model.addRow(row);
                    }
                }
                
                // Show message if no results found
                if (model.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(SupervisorDashboard.this,
                        "No submissions match your search criteria.",
                        "No Results",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        // Update details when a row is selected
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                String project = table.getValueAt(row, 0).toString();
                String student = table.getValueAt(row, 1).toString();
                String version = table.getValueAt(row, 2).toString();
                String date = table.getValueAt(row, 3).toString();
                String fileType = table.getValueAt(row, 4).toString();
                String fileSize = table.getValueAt(row, 5).toString();
                
                // Get additional details for the submission
                String submissionDetails = getSubmissionDetails(project, student);
                
                // Update details area
                detailsArea.setText(
                    "Project: " + project + "\n" +
                    "Student: " + student + "\n" +
                    "Version: " + version + "\n" +
                    "Submitted: " + date + "\n" +
                    "File Type: " + fileType + "\n" +
                    "File Size: " + fileSize + "\n\n" +
                    submissionDetails
                );
            } else {
                detailsArea.setText("Select a submission to view details");
            }
        });
        
        // View file action
        viewFileButton.addActionListener(e -> {
            if (table.getSelectedRow() != -1) {
                String project = table.getValueAt(table.getSelectedRow(), 0).toString();
                String fileType = table.getValueAt(table.getSelectedRow(), 4).toString();
                
                JOptionPane.showMessageDialog(SupervisorDashboard.this,
                    "Opening file for project: " + project + "\n" +
                    "File type: " + fileType + "\n\n" +
                    "In a production environment, this would open the file in an appropriate viewer.",
                    "View File",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(SupervisorDashboard.this,
                    "Please select a submission first",
                    "No Selection",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Download file action
        downloadButton.addActionListener(e -> {
            if (table.getSelectedRow() != -1) {
                String project = table.getValueAt(table.getSelectedRow(), 0).toString();
                String fileType = table.getValueAt(table.getSelectedRow(), 4).toString();
                String fileSize = table.getValueAt(table.getSelectedRow(), 5).toString();
                
                JOptionPane.showMessageDialog(SupervisorDashboard.this,
                    "Downloading file for project: " + project + "\n" +
                    "File type: " + fileType + "\n" +
                    "File size: " + fileSize + "\n\n" +
                    "In a production environment, this would save the file to your computer.",
                    "Download File",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(SupervisorDashboard.this,
                    "Please select a submission first",
                    "No Selection",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Add action listeners for other buttons
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String project = table.getValueAt(selectedRow, 0).toString();
                    String student = table.getValueAt(selectedRow, 1).toString();
                    String version = table.getValueAt(selectedRow, 2).toString();
                    
                    // Create a more detailed view dialog
                    JDialog viewDialog = new JDialog(SupervisorDashboard.this, "Submission Details - " + project, true);
                    viewDialog.setLayout(new BorderLayout());
                    viewDialog.setSize(600, 400);
                    viewDialog.setLocationRelativeTo(SupervisorDashboard.this);
                    
                    JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 5));
                    infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    
                    infoPanel.add(new JLabel("Project:"));
                    infoPanel.add(new JLabel(project));
                    
                    infoPanel.add(new JLabel("Student:"));
                    infoPanel.add(new JLabel(student));
                    
                    infoPanel.add(new JLabel("Version:"));
                    infoPanel.add(new JLabel(version));
                    
                    infoPanel.add(new JLabel("Submission Date:"));
                    infoPanel.add(new JLabel(table.getValueAt(selectedRow, 3).toString()));
                    
                    infoPanel.add(new JLabel("File Type:"));
                    infoPanel.add(new JLabel(table.getValueAt(selectedRow, 4).toString()));
                    
                    infoPanel.add(new JLabel("File Size:"));
                    infoPanel.add(new JLabel(table.getValueAt(selectedRow, 5).toString()));
                    
                    // Add additional submission information
                    JTextArea submissionText = new JTextArea(getSubmissionDetails(project, student));
                    submissionText.setEditable(false);
                    submissionText.setLineWrap(true);
                    submissionText.setWrapStyleWord(true);
                    
                    JScrollPane textScrollPane = new JScrollPane(submissionText);
                    textScrollPane.setBorder(BorderFactory.createTitledBorder("Submission Information"));
                    
                    JPanel contentPanel = new JPanel(new BorderLayout());
                    contentPanel.add(infoPanel, BorderLayout.NORTH);
                    contentPanel.add(textScrollPane, BorderLayout.CENTER);
                    
                    JButton closeButton = new JButton("Close");
                    closeButton.addActionListener(event -> viewDialog.dispose());
                    
                    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                    buttonPanel.add(closeButton);
                    
                    viewDialog.add(contentPanel, BorderLayout.CENTER);
                    viewDialog.add(buttonPanel, BorderLayout.SOUTH);
                    viewDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(SupervisorDashboard.this, 
                        "Please select a submission to view", 
                        "Selection Required", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        // Provide feedback without approval/rejection
        provideFeedbackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String project = table.getValueAt(selectedRow, 0).toString();
                    String student = table.getValueAt(selectedRow, 1).toString();
                    
                    // Create feedback dialog
                    JDialog feedbackDialog = new JDialog(SupervisorDashboard.this, "Provide Feedback", true);
                    feedbackDialog.setLayout(new BorderLayout());
                    feedbackDialog.setSize(500, 400);
                    feedbackDialog.setLocationRelativeTo(SupervisorDashboard.this);
                    
                    JPanel headerPanel = new JPanel(new GridLayout(3, 2, 5, 5));
                    headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    
                    headerPanel.add(new JLabel("Project:"));
                    headerPanel.add(new JLabel(project));
                    
                    headerPanel.add(new JLabel("Student:"));
                    headerPanel.add(new JLabel(student));
                    
                    headerPanel.add(new JLabel("Feedback Type:"));
                    JComboBox<String> feedbackTypeCombo = new JComboBox<>(new String[]{
                        "General Feedback", "Technical Issues", "Content Improvement", "Format Suggestions"
                    });
                    headerPanel.add(feedbackTypeCombo);
                    
                    JLabel instructionsLabel = new JLabel("Enter your feedback below:");
                    
                    JTextArea feedbackArea = new JTextArea(10, 40);
                    feedbackArea.setLineWrap(true);
                    feedbackArea.setWrapStyleWord(true);
                    
                    JScrollPane scrollPane = new JScrollPane(feedbackArea);
                    
                    JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                    JButton cancelButton = new JButton("Cancel");
                    JButton sendButton = new JButton("Send Feedback");
                    
                    controlPanel.add(cancelButton);
                    controlPanel.add(sendButton);
                    
                    JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
                    contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    contentPanel.add(headerPanel, BorderLayout.NORTH);
                    contentPanel.add(instructionsLabel, BorderLayout.CENTER);
                    contentPanel.add(scrollPane, BorderLayout.SOUTH);
                    
                    feedbackDialog.add(contentPanel, BorderLayout.CENTER);
                    feedbackDialog.add(controlPanel, BorderLayout.SOUTH);
                    
                    // Add action listeners
                    cancelButton.addActionListener(event -> feedbackDialog.dispose());
                    
                    sendButton.addActionListener(event -> {
                        if (feedbackArea.getText().trim().isEmpty()) {
                            JOptionPane.showMessageDialog(feedbackDialog,
                                "Please enter feedback before sending",
                                "Empty Feedback",
                                JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        
                        String feedbackType = (String) feedbackTypeCombo.getSelectedItem();
                        
                        JOptionPane.showMessageDialog(feedbackDialog,
                            "Feedback sent successfully to " + student + ".\n\n" +
                            "Type: " + feedbackType + "\n\n" +
                            "In a production environment, this would be saved to the database and notified to the student.",
                            "Feedback Sent",
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        feedbackDialog.dispose();
                    });
                    
                    feedbackDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(SupervisorDashboard.this, 
                        "Please select a submission to provide feedback", 
                        "Selection Required", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        // Enhanced approve functionality with grading
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String project = table.getValueAt(selectedRow, 0).toString();
                    String student = table.getValueAt(selectedRow, 1).toString();
                    
                    // Create grade panel
                    JPanel gradePanel = new JPanel(new BorderLayout(10, 10));
                    gradePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    
                    JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
                    
                    inputPanel.add(new JLabel("Grade (0-100):"));
                    JSpinner gradeSpinner = new JSpinner(new SpinnerNumberModel(75, 0, 100, 1));
                    inputPanel.add(gradeSpinner);
                    
                    inputPanel.add(new JLabel("Status:"));
                    JComboBox<String> statusCombo = new JComboBox<>(new String[]{
                        "Approved", "Approved with Minor Revisions", "Approved with Distinction"
                    });
                    inputPanel.add(statusCombo);
                    
                    inputPanel.add(new JLabel("Comments:"));
                    JTextArea commentsArea = new JTextArea(5, 30);
                    commentsArea.setLineWrap(true);
                    commentsArea.setWrapStyleWord(true);
                    
                    JScrollPane commentsScrollPane = new JScrollPane(commentsArea);
                    
                    gradePanel.add(inputPanel, BorderLayout.NORTH);
                    gradePanel.add(commentsScrollPane, BorderLayout.CENTER);
                    
                    int result = JOptionPane.showConfirmDialog(SupervisorDashboard.this,
                        gradePanel,
                        "Approve Submission - " + project,
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);
                    
                    if (result == JOptionPane.OK_OPTION) {
                        int grade = (Integer) gradeSpinner.getValue();
                        String status = (String) statusCombo.getSelectedItem();
                        String comments = commentsArea.getText();
                        
                        JOptionPane.showMessageDialog(SupervisorDashboard.this, 
                            "Submission approved for:\n" +
                            "Project: " + project + "\n" +
                            "Student: " + student + "\n" +
                            "Grade: " + grade + "\n" +
                            "Status: " + status + "\n" +
                            "Comments: " + (comments.isEmpty() ? "None" : comments),
                            "Approved", 
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        // Remove the approved submission from the table
                        model.removeRow(table.convertRowIndexToModel(selectedRow));
                    }
                } else {
                    JOptionPane.showMessageDialog(SupervisorDashboard.this, 
                        "Please select a submission to approve", 
                        "Selection Required", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        // Enhanced reject functionality with categorized reasons
        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String project = table.getValueAt(selectedRow, 0).toString();
                    String student = table.getValueAt(selectedRow, 1).toString();
                    
                    // Create panel for rejection reasons
                    JPanel rejectPanel = new JPanel(new BorderLayout(10, 10));
                    rejectPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    
                    JPanel topPanel = new JPanel(new GridLayout(2, 2, 5, 5));
                    
                    topPanel.add(new JLabel("Rejection Category:"));
                    JComboBox<String> reasonCategory = new JComboBox<>(new String[]{
                        "Incomplete Submission",
                        "Quality Issues",
                        "Format/Structure Problems",
                        "Content Issues",
                        "Plagiarism Concerns",
                        "Other"
                    });
                    topPanel.add(reasonCategory);
                    
                    topPanel.add(new JLabel("Action Required:"));
                    JComboBox<String> actionCombo = new JComboBox<>(new String[]{
                        "Resubmit with Changes",
                        "Major Revision Needed",
                        "Complete Rewrite Required",
                        "Academic Integrity Meeting Required"
                    });
                    topPanel.add(actionCombo);
                    
                    JLabel detailLabel = new JLabel("Detailed explanation (required):");
                    
                    JTextArea reasonArea = new JTextArea(5, 30);
                    reasonArea.setLineWrap(true);
                    reasonArea.setWrapStyleWord(true);
                    
                    JScrollPane reasonScrollPane = new JScrollPane(reasonArea);
                    
                    rejectPanel.add(topPanel, BorderLayout.NORTH);
                    rejectPanel.add(detailLabel, BorderLayout.CENTER);
                    rejectPanel.add(reasonScrollPane, BorderLayout.SOUTH);
                    
                    int result = JOptionPane.showConfirmDialog(SupervisorDashboard.this,
                        rejectPanel,
                        "Reject Submission - " + project,
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);
                    
                    if (result == JOptionPane.OK_OPTION) {
                        String category = (String) reasonCategory.getSelectedItem();
                        String action = (String) actionCombo.getSelectedItem();
                        String details = reasonArea.getText();
                        
                        if (details.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(SupervisorDashboard.this,
                                "Please provide detailed feedback for the rejection",
                                "Missing Information",
                                JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        JOptionPane.showMessageDialog(SupervisorDashboard.this, 
                            "Submission rejected for:\n" +
                            "Project: " + project + "\n" +
                            "Student: " + student + "\n" +
                            "Category: " + category + "\n" +
                            "Action Required: " + action + "\n" +
                            "Detailed Feedback: " + details,
                            "Rejected", 
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        // Remove the rejected submission from the table
                        model.removeRow(table.convertRowIndexToModel(selectedRow));
                    }
                } else {
                    JOptionPane.showMessageDialog(SupervisorDashboard.this, 
                        "Please select a submission to reject", 
                        "Selection Required", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        return panel;
    }
    
    /**
     * Helper method to retrieve detailed information about a submission
     */
    private String getSubmissionDetails(String project, String student) {
        // In a real application, this would retrieve data from the database
        if (project.equals("Final Year Project") && student.equals("inam Student")) {
            return "Abstract: An analysis of machine learning applications in healthcare.\n\n" +
                   "Previous versions: Initial proposal (v0.5) submitted on 2023-03-15.\n\n" +
                   "Student Notes: Added implementation details and results from preliminary testing.";
        } else if (project.equals("Research Paper") && student.equals("aftab Student")) {
            return "Abstract: Examining the impact of social media on academic performance.\n\n" +
                   "Previous feedback: Good literature review but methodology needs improvement.\n\n" +
                   "This version addresses previous methodology concerns and includes additional data analysis.";
        } else if (project.equals("Group Assignment") && student.equals("shakir Student")) {
            return "Team Members: shakir Student (leader), inam, aftab\n\n" +
                   "Project Topic: Climate change impact analysis using GIS\n\n" +
                   "shakir Contribution: Data collection and analysis (40%)";
        } 
        return "No detailed information available for this submission.";
    }
    
    /**
     * Creates the grade panel.
     */
    private JPanel createGradePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Grade Projects");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Project selection
        JLabel projectLabel = new JLabel("Select Project:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(projectLabel, gbc);
        
        JComboBox<String> projectComboBox = new JComboBox<>(new String[]{
            "Final Year Project - inam Student",
            "Research Paper - aftab Student",
            "Group Assignment - shakir Student"
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(projectComboBox, gbc);
        
        // Grade components
        JPanel gradeComponentsPanel = new JPanel(new GridLayout(4, 3, 5, 5));
        gradeComponentsPanel.setBorder(BorderFactory.createTitledBorder("Grade Components"));
        
        gradeComponentsPanel.add(new JLabel("Component"));
        gradeComponentsPanel.add(new JLabel("Weight (%)"));
        gradeComponentsPanel.add(new JLabel("Score (0-100)"));
        
        JTextField component1Field = new JTextField("Research");
        JTextField weight1Field = new JTextField("30");
        JSpinner score1Spinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        
        gradeComponentsPanel.add(component1Field);
        gradeComponentsPanel.add(weight1Field);
        gradeComponentsPanel.add(score1Spinner);
        
        JTextField component2Field = new JTextField("Methodology");
        JTextField weight2Field = new JTextField("30");
        JSpinner score2Spinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        
        gradeComponentsPanel.add(component2Field);
        gradeComponentsPanel.add(weight2Field);
        gradeComponentsPanel.add(score2Spinner);
        
        JTextField component3Field = new JTextField("Presentation");
        JTextField weight3Field = new JTextField("40");
        JSpinner score3Spinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        
        gradeComponentsPanel.add(component3Field);
        gradeComponentsPanel.add(weight3Field);
        gradeComponentsPanel.add(score3Spinner);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(gradeComponentsPanel, gbc);
        
        // Final grade
        JPanel finalGradePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        finalGradePanel.add(new JLabel("Final Grade:"));
        
        JTextField finalGradeField = new JTextField(5);
        finalGradeField.setEditable(false);
        finalGradePanel.add(finalGradeField);
        
        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double weight1 = Double.parseDouble(weight1Field.getText()) / 100.0;
                    double weight2 = Double.parseDouble(weight2Field.getText()) / 100.0;
                    double weight3 = Double.parseDouble(weight3Field.getText()) / 100.0;
                    
                    int score1 = (Integer) score1Spinner.getValue();
                    int score2 = (Integer) score2Spinner.getValue();
                    int score3 = (Integer) score3Spinner.getValue();
                    
                    double finalGrade = (weight1 * score1) + (weight2 * score2) + (weight3 * score3);
                    finalGradeField.setText(String.format("%.1f", finalGrade));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(SupervisorDashboard.this, 
                        "Invalid weight values. Please enter numeric values.", 
                        "Calculation Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        finalGradePanel.add(calculateButton);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(finalGradePanel, gbc);
        
        // Comments
        JLabel commentsLabel = new JLabel("Comments:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(commentsLabel, gbc);
        
        JTextArea commentsArea = new JTextArea(5, 30);
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(commentsArea);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(scrollPane, gbc);
        
        // Submit button
        JButton submitButton = new JButton("Submit Grade");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (finalGradeField.getText().isEmpty() || commentsArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(SupervisorDashboard.this, 
                        "Please calculate the final grade and provide comments", 
                        "Grading Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                JOptionPane.showMessageDialog(SupervisorDashboard.this, 
                    "Grade submitted successfully!\n\n" +
                    "Project: " + projectComboBox.getSelectedItem() + "\n" +
                    "Final Grade: " + finalGradeField.getText(),
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Clear form
                projectComboBox.setSelectedIndex(0);
                score1Spinner.setValue(0);
                score2Spinner.setValue(0);
                score3Spinner.setValue(0);
                finalGradeField.setText("");
                commentsArea.setText("");
            }
        });
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(submitButton, gbc);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Handles the logout process.
     */
    private void logout() {
        // Logout the user
        UserController.getInstance().logout();
        
        // Close the dashboard
        this.dispose();
        
        // Show the login form
        LoginForm loginForm = new LoginForm();
        loginForm.setVisible(true);
    }
}
