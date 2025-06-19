package com.university.view;

import com.university.controller.UserController;
import com.university.model.Role;
import com.university.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.university.model.Activity;
import com.university.model.Notification; // Import the Notification class


/**
 * Dashboard for administrator users.
 * Provides access to admin-specific functionalities.
 * Features responsive design to adapt to different screen sizes.
 */
public class AdminDashboard extends JFrame {
    private final User currentUser;
    private final JPanel contentPanel;
    private final JPanel sidebarPanel;
    private final JPanel mainPanel;
    
    // Minimum dimensions for the dashboard
    private static final int MIN_WIDTH = 1000;
    private static final int MIN_HEIGHT = 800;
    
    // Sidebar width variables
    private static final int SIDEBAR_EXPANDED_WIDTH = 200;
    private static final int SIDEBAR_COLLAPSED_WIDTH = 60;
    private boolean sidebarCollapsed = false;
    
    public AdminDashboard(User user) {
        this.currentUser = user;
        
        // Set up the frame
        setTitle("Admin Dashboard - " + user.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setSize(1200, 800); // Larger initial size
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
        JPanel manageUsersPanel = createManageUsersPanel();
        JPanel notificationsPanel = createNotificationsPanel();
        JPanel viewStudentsPanel = createViewStudentsPanel();
        JPanel defineDeadlinesPanel = createDefineDeadlinesPanel();
        
        contentPanel.add(welcomePanel, "welcome");
        contentPanel.add(manageUsersPanel, "manageUsers");
        contentPanel.add(notificationsPanel, "notifications");
        contentPanel.add(viewStudentsPanel, "viewStudents");
        contentPanel.add(defineDeadlinesPanel, "defineDeadlines");
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add a status bar at the bottom
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.setPreferredSize(new Dimension(getWidth(), 25));
        JLabel statusLabel = new JLabel(" System Status: Online");
        statusBar.add(statusLabel, BorderLayout.WEST);
        
        // Add current date to status bar
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JLabel dateLabel = new JLabel(dateFormat.format(new Date()) + " ");
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
        if (width < 700 && !sidebarCollapsed) {
            collapseSidebar();
        } else if (width >= 700 && sidebarCollapsed) {
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
        
        if (width < 700) {
            headerFont = new Font("Arial", Font.BOLD, 14);
            regularFont = new Font("Arial", Font.PLAIN, 12);
            smallFont = new Font("Arial", Font.PLAIN, 10);
        } else if (width < 1000) {
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
        String[] buttonTexts = {"Dashboard", "Manage Users", "Notifications", 
                               "View Students", "Define Deadlines"};
        
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
        panel.setBackground(new Color(230, 230, 250));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 220)),
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
        JLabel userInfoLabel = new JLabel("Logged in as: " + currentUser.getFullName() + " (Administrator)");
        userInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userInfoPanel.add(userInfoLabel);
        
        panel.add(userInfoPanel, BorderLayout.WEST);
        
        // Right side panel with buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        
        // Settings button
        JButton settingsButton = new JButton("Settings");
        settingsButton.setFocusPainted(false);
        rightPanel.add(settingsButton);
        
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
        g2d.setColor(new Color(100, 50, 150)); // Purple color for admin
        g2d.fillOval(0, 0, size, size);
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    /**
     * Creates the sidebar panel with navigation buttons.
     */
    private JPanel createSidebarPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 0, 10));
        panel.setBackground(new Color(240, 240, 245));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(200, 200, 220)),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
        panel.setPreferredSize(new Dimension(SIDEBAR_EXPANDED_WIDTH, getHeight()));
        
        // Create navigation buttons
        JButton dashboardButton = createNavButton("Dashboard", "welcome");
        JButton manageUsersButton = createNavButton("Manage Users", "manageUsers");
        JButton notificationsButton = createNavButton("Notifications", "notifications");
        JButton viewStudentsButton = createNavButton("View Students", "viewStudents");
        JButton defineDeadlinesButton = createNavButton("Define Deadlines", "defineDeadlines");
        
        // Add buttons to panel
        panel.add(dashboardButton);
        panel.add(manageUsersButton);
        panel.add(notificationsButton);
        panel.add(viewStudentsButton);
        panel.add(defineDeadlinesButton);
        
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
     * Creates the welcome panel with system overview.
     */
  private JPanel createWelcomePanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JLabel welcomeLabel = new JLabel("Welcome to the Admin Dashboard, " + currentUser.getFullName() + "!");
    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
    panel.add(welcomeLabel, BorderLayout.NORTH);

    // System statistics panel
    JPanel statsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
    statsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "System Statistics"));

    UserController userController = UserController.getInstance();
    statsPanel.add(new JLabel("Total Users:"));
    statsPanel.add(new JLabel(String.valueOf(userController.getTotalUsers()))); // Dynamically fetch data

    statsPanel.add(new JLabel("Active Students:"));
    statsPanel.add(new JLabel(String.valueOf(userController.getActiveStudents()))); // Dynamically fetch data

    statsPanel.add(new JLabel("Active Supervisors:"));
    statsPanel.add(new JLabel(String.valueOf(userController.getActiveSupervisors()))); // Dynamically fetch data

    statsPanel.add(new JLabel("Projects in Progress:"));
    statsPanel.add(new JLabel(String.valueOf(userController.getProjectsInProgressCount()))); // Dynamically fetch data

    JPanel centerPanel = new JPanel(new BorderLayout());
    centerPanel.add(statsPanel, BorderLayout.NORTH);

    // Recent activity panel
    JPanel activityPanel = new JPanel(new BorderLayout());
    activityPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Recent Activity"));

    String[] columnNames = {"User", "Action", "Time"};
    
    // Fetch recent activity from the database
    List<Activity> recentActivity = userController.getRecentActivity();
    Object[][] data = new Object[recentActivity.size()][3];
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    for (int i = 0; i < recentActivity.size(); i++) {
        Activity activity = recentActivity.get(i);
        data[i][0] = activity.getUserName();
        data[i][1] = activity.getAction();
        data[i][2] = dateFormat.format(activity.getTime());
    }

    JTable activityTable = new JTable(data, columnNames);
    activityTable.setEnabled(false); // Read-only table
    JScrollPane scrollPane = new JScrollPane(activityTable);
    scrollPane.setPreferredSize(new Dimension(500, 150));

    activityPanel.add(scrollPane, BorderLayout.CENTER);
    centerPanel.add(activityPanel, BorderLayout.CENTER);

    // System performance tips
    JPanel tipsPanel = new JPanel(new BorderLayout());
    tipsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "System Tips"));

    JTextArea tipsArea = new JTextArea(
        "• Remember to regularly backup the system database\n" +
        "• Set realistic deadlines for projects to ensure quality submissions\n" +
        "• Encourage supervisors to provide timely feedback to students\n" +
        "• Monitor student progress and send reminders for approaching deadlines"
    );
    tipsArea.setEditable(false);
    tipsArea.setLineWrap(true);
    tipsArea.setWrapStyleWord(true);
    tipsArea.setBackground(panel.getBackground());

    tipsPanel.add(tipsArea, BorderLayout.CENTER);
    centerPanel.add(tipsPanel, BorderLayout.SOUTH);

    panel.add(centerPanel, BorderLayout.CENTER);

    return panel;
}

    
    /**
     * Creates the manage users panel.
     */
    private JPanel createManageUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Manage Users");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Create a table to display users
        UserController userController = UserController.getInstance();
        List<User> allUsers = userController.getAllUsers();
        
        String[] columnNames = {"ID", "Username", "Full Name", "Email", "Role"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table read-only
            }
        };
        
        // Add users to table model (or use placeholder data if empty)
        if (allUsers.isEmpty()) {
            tableModel.addRow(new Object[]{1, "student1", "inam Student", "inam@university.edu", "Student"});
            tableModel.addRow(new Object[]{2, "supervisor1", "ali Supervisor", "ali@university.edu", "Supervisor"});
            tableModel.addRow(new Object[]{3, "admin1", "Admin User", "admin@university.edu", "Admin"});
        } else {
            for (User user : allUsers) {
                tableModel.addRow(new Object[]{
                    user.getId(),
                    user.getUsername(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getRole().getDisplayName()
                });
            }
        }
        
        JTable userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setAutoCreateRowSorter(true);
        
        JScrollPane tableScrollPane = new JScrollPane(userTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Create panel for user management actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton addButton = new JButton("Add User");
        JButton editButton = new JButton("Edit User");
        JButton deleteButton = new JButton("Delete User");
        JButton resetPasswordButton = new JButton("Reset Password");
        
        actionPanel.add(addButton);
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);
        actionPanel.add(resetPasswordButton);
        
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddUserDialog(tableModel);
            }
        });
        
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedRow = userTable.convertRowIndexToModel(selectedRow);
                    showEditUserDialog(tableModel, selectedRow);
                } else {
                    JOptionPane.showMessageDialog(AdminDashboard.this,
                        "Please select a user to edit",
                        "Selection Required",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedRow = userTable.convertRowIndexToModel(selectedRow);
                    int userId = (Integer) tableModel.getValueAt(selectedRow, 0);
                    
                    int result = JOptionPane.showConfirmDialog(AdminDashboard.this,
                        "Are you sure you want to delete this user?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION);
                        
                    if (result == JOptionPane.YES_OPTION) {
                        if (userController.deleteUser(userId)) {
                            tableModel.removeRow(selectedRow);
                            JOptionPane.showMessageDialog(AdminDashboard.this,
                                "User deleted successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(AdminDashboard.this,
                                "Failed to delete user",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(AdminDashboard.this,
                        "Please select a user to delete",
                        "Selection Required",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        resetPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedRow = userTable.convertRowIndexToModel(selectedRow);
                    String username = (String) tableModel.getValueAt(selectedRow, 1);
                    
                    String newPassword = JOptionPane.showInputDialog(AdminDashboard.this,
                        "Enter new password for user " + username + ":",
                        "Reset Password",
                        JOptionPane.QUESTION_MESSAGE);
                        
                    if (newPassword != null && !newPassword.isEmpty()) {
                        // In a real system, we would update the user's password here
                        JOptionPane.showMessageDialog(AdminDashboard.this,
                            "Password reset successfully for user " + username,
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(AdminDashboard.this,
                        "Please select a user to reset password",
                        "Selection Required",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        return panel;
    }
    
    /**
     * Shows a dialog for adding a new user.
     */
    private void showAddUserDialog(DefaultTableModel tableModel) {
        JDialog dialog = new JDialog(this, "Add New User", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        
        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Password:"), gbc);
        
        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(passwordField, gbc);
        
        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Full Name:"), gbc);
        
        JTextField fullNameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(fullNameField, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Email:"), gbc);
        
        JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(emailField, gbc);
        
        // Role
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Role:"), gbc);
        
        JComboBox<Role> roleComboBox = new JComboBox<>(Role.values());
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(roleComboBox, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(buttonPanel, gbc);
        
        // Add action listeners
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String fullName = fullNameField.getText();
                String email = emailField.getText();
                Role role = (Role) roleComboBox.getSelectedItem();
                
                if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                        "All fields are required",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Create new user
                UserController userController = UserController.getInstance();
                User newUser = userController.register(username, password, fullName, email, role);
                
                if (newUser != null) {
                    // Add to table
                    tableModel.addRow(new Object[]{
                        newUser.getId(),
                        newUser.getUsername(),
                        newUser.getFullName(),
                        newUser.getEmail(),
                        newUser.getRole().getDisplayName()
                    });
                    
                    JOptionPane.showMessageDialog(dialog,
                        "User added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                        "Failed to add user. Username may already exist.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        dialog.getContentPane().add(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Shows a dialog for editing an existing user.
     */
    private void showEditUserDialog(DefaultTableModel tableModel, int row) {
        JDialog dialog = new JDialog(this, "Edit User", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Get user data from table
        int userId = (Integer) tableModel.getValueAt(row, 0);
        String username = (String) tableModel.getValueAt(row, 1);
        String fullName = (String) tableModel.getValueAt(row, 2);
        String email = (String) tableModel.getValueAt(row, 3);
        String roleName = (String) tableModel.getValueAt(row, 4);
        
        // Username (read-only)
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        
        JTextField usernameField = new JTextField(username, 20);
        usernameField.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(usernameField, gbc);
        
        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Full Name:"), gbc);
        
        JTextField fullNameField = new JTextField(fullName, 20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(fullNameField, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Email:"), gbc);
        
        JTextField emailField = new JTextField(email, 20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(emailField, gbc);
        
        // Role
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Role:"), gbc);
        
        JComboBox<Role> roleComboBox = new JComboBox<>(Role.values());
        roleComboBox.setSelectedItem(Role.fromString(roleName));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(roleComboBox, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(buttonPanel, gbc);
        
        // Add action listeners
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newFullName = fullNameField.getText();
                String newEmail = emailField.getText();
                Role newRole = (Role) roleComboBox.getSelectedItem();
                
                if (newFullName.isEmpty() || newEmail.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                        "Full name and email are required",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Update user
                UserController userController = UserController.getInstance();
                User user = new User(userId, username, "", newFullName, newEmail, newRole);
                
                if (userController.updateUser(user)) {
                    // Update table
                    tableModel.setValueAt(newFullName, row, 2);
                    tableModel.setValueAt(newEmail, row, 3);
                    tableModel.setValueAt(newRole.getDisplayName(), row, 4);
                    
                    JOptionPane.showMessageDialog(dialog,
                        "User updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                        "Failed to update user",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        dialog.getContentPane().add(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Creates the notifications panel.
     */
private JPanel createNotificationsPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JLabel titleLabel = new JLabel("Manage Notifications");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    panel.add(titleLabel, BorderLayout.NORTH);

    JTabbedPane tabbedPane = new JTabbedPane();

    // Send notifications tab
    JPanel sendPanel = new JPanel(new BorderLayout());
    sendPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JPanel formPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.anchor = GridBagConstraints.WEST;

    // Recipients
    gbc.gridx = 0;
    gbc.gridy = 0;
    formPanel.add(new JLabel("Recipients:"), gbc);

    JPanel recipientsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JRadioButton allUsersRadio = new JRadioButton("All Users");
    JRadioButton studentsRadio = new JRadioButton("Students");
    JRadioButton supervisorsRadio = new JRadioButton("Supervisors");
    JRadioButton specificUserRadio = new JRadioButton("Specific User");

    ButtonGroup recipientGroup = new ButtonGroup();
    recipientGroup.add(allUsersRadio);
    recipientGroup.add(studentsRadio);
    recipientGroup.add(supervisorsRadio);
    recipientGroup.add(specificUserRadio);

    allUsersRadio.setSelected(true);

    recipientsPanel.add(allUsersRadio);
    recipientsPanel.add(studentsRadio);
    recipientsPanel.add(supervisorsRadio);
    recipientsPanel.add(specificUserRadio);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    formPanel.add(recipientsPanel, gbc);

    // Define the JComboBox here
    JComboBox<String> userComboBox = new JComboBox<>();

    // Fetch users from the database and populate the ComboBox
    UserController userController = UserController.getInstance();
    List<User> allUsers = userController.getAllUsers();

    // Populate the JComboBox with user names (or any other field you need)
    for (User user : allUsers) {
        userComboBox.addItem(user.getFullName() + " (" + user.getUsername() + ")");
    }
    userComboBox.setEnabled(false);

    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    formPanel.add(userComboBox, gbc);

    // Enable user selector only when "Specific User" is selected
    specificUserRadio.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            userComboBox.setEnabled(specificUserRadio.isSelected());
        }
    });

    // Subject
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.fill = GridBagConstraints.NONE;
    formPanel.add(new JLabel("Subject:"), gbc);

    JTextField subjectField = new JTextField(30);
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    formPanel.add(subjectField, gbc);

    // Message
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.fill = GridBagConstraints.NONE;
    formPanel.add(new JLabel("Message:"), gbc);

    JTextArea messageArea = new JTextArea(8, 30);
    messageArea.setLineWrap(true);
    messageArea.setWrapStyleWord(true);
    JScrollPane messageScrollPane = new JScrollPane(messageArea);

    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.fill = GridBagConstraints.BOTH;
    formPanel.add(messageScrollPane, gbc);

    // Importance
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.fill = GridBagConstraints.NONE;
    formPanel.add(new JLabel("Importance:"), gbc);

    JComboBox<String> importanceComboBox = new JComboBox<>(new String[]{"Normal", "High", "Urgent"});
    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    formPanel.add(importanceComboBox, gbc);

    // Send button
    JButton sendButton = new JButton("Send Notification");
  sendButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Validate input fields (Subject, Message, etc.)
        if (subjectField.getText().isEmpty() || messageArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(AdminDashboard.this,
                    "Subject and message are required",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get the selected recipient group
        String recipients = "";
        int recipientId = -1;  // -1 means all users

        if (allUsersRadio.isSelected()) {
            recipients = "All Users";
        } else if (studentsRadio.isSelected()) {
            recipients = "All Students";
        } else if (supervisorsRadio.isSelected()) {
            recipients = "All Supervisors";
        } else {
            recipients = userComboBox.getSelectedItem().toString();
            recipientId = allUsers.get(userComboBox.getSelectedIndex()).getId(); // Get the ID of the selected user
        }

        // Call the method to send notification
        UserController.getInstance().sendNotification(recipientId, subjectField.getText(), messageArea.getText(), importanceComboBox.getSelectedItem().toString());


        // Show confirmation message
        JOptionPane.showMessageDialog(AdminDashboard.this,
                "Notification sent successfully to: " + recipients,
                "Notification Sent",
                JOptionPane.INFORMATION_MESSAGE);

        // Clear form after sending
        subjectField.setText("");
        messageArea.setText("");
        importanceComboBox.setSelectedIndex(0);
        allUsersRadio.setSelected(true);
        userComboBox.setEnabled(false); // Disable the user combo box for non "Specific User" options
    }
});

    gbc.gridx = 1;
    gbc.gridy = 5;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.EAST;
    formPanel.add(sendButton, gbc);

    sendPanel.add(formPanel, BorderLayout.CENTER);

    // View sent notifications tab
    JPanel viewPanel = new JPanel(new BorderLayout());
    viewPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Fetch notifications sent by the admin
    List<Notification> notifications = UserController.getInstance().getSentNotifications(currentUser.getId()); // Fetch notifications for the admin

    // Check if there are no notifications
    if (notifications == null || notifications.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No notifications found", "Information", JOptionPane.INFORMATION_MESSAGE);
        // Exit method if no notifications are available
    }

    String[] columnNames = {"Date", "Subject", "Recipients", "Importance"};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Add notifications to the table
    for (Notification notification : notifications) {
        // Ensure none of the values are null before adding them to the table
        String formattedDate = (notification.getSendDate() != null) ? dateFormat.format(notification.getSendDate()) : "No Date";
        String subject = (notification.getSubject() != null) ? notification.getSubject() : "No Subject";
        String message = (notification.getMessage() != null) ? notification.getMessage() : "No Message";
        String importance = (notification.getImportance() != null) ? notification.getImportance() : "Normal";

        tableModel.addRow(new Object[]{
            formattedDate,
            subject,
            message,
            importance
        });
    }

    JTable notificationsTable = new JTable(tableModel);
    JScrollPane tableScrollPane = new JScrollPane(notificationsTable);
    viewPanel.add(tableScrollPane, BorderLayout.CENTER);

    // Add tabs to tabbed pane
    tabbedPane.addTab("Send Notification", sendPanel);
    tabbedPane.addTab("View Sent Notifications", viewPanel);

    panel.add(tabbedPane, BorderLayout.CENTER);

    return panel; // Return the panel
}



    
    /**
     * Creates the view students panel.
     */
private JPanel createViewStudentsPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
    JLabel titleLabel = new JLabel("View Students");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    panel.add(titleLabel, BorderLayout.NORTH);
    
    // Fetch the students with projects
    List<User> studentsWithProjects = UserController.getInstance().getAllUsersWithProjects();
    
    // Table columns
    String[] columnNames = {"ID", "Name", "Email", "Project Title", "Supervisor", "Status"};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Prevent editing
        }
    };
    
    // Add the student project data to the table
    for (User student : studentsWithProjects) {
        tableModel.addRow(new Object[]{
            student.getId(),
            student.getFullName(),
            student.getEmail(),
            student.getProjectName(),
            student.getSupervisorName(),
            student.getStatus()
        });
    }

    // Create the table to display the student data
    JTable studentTable = new JTable(tableModel);
    studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    studentTable.setAutoCreateRowSorter(true);
    JScrollPane scrollPane = new JScrollPane(studentTable);
    panel.add(scrollPane, BorderLayout.CENTER);
    
    return panel;
}



    
    /**
     * Creates the define deadlines panel.
     */
    private JPanel createDefineDeadlinesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Define Deadlines");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Current deadlines tab
        JPanel currentPanel = new JPanel(new BorderLayout());
        
        String[] columnNames = {"Project Type", "Deadline", "Description", "Created By"};
        Object[][] data = {
            {"Final Year Project", "2023-06-30", "Final submission", "Admin User"},
            {"Research Paper", "2023-04-30", "Final version", "Admin User"},
            {"Group Assignment", "2023-03-31", "All group members must submit", "Admin User"},
            {"Thesis", "2023-07-15", "Including presentation", "Admin User"}
        };
        
        JTable deadlinesTable = new JTable(data, columnNames);
        JScrollPane tableScrollPane = new JScrollPane(deadlinesTable);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editButton = new JButton("Edit Selected");
        JButton deleteButton = new JButton("Delete Selected");
        
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        currentPanel.add(tableScrollPane, BorderLayout.CENTER);
        currentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add new deadline tab
        JPanel newPanel = new JPanel(new GridBagLayout());
        newPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Project type
        gbc.gridx = 0;
        gbc.gridy = 0;
        newPanel.add(new JLabel("Project Type:"), gbc);
        
        JTextField projectTypeField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        newPanel.add(projectTypeField, gbc);
        
        // Deadline date
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        newPanel.add(new JLabel("Deadline Date:"), gbc);
        
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
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        newPanel.add(datePanel, gbc);
        
        // Description
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        newPanel.add(new JLabel("Description:"), gbc);
        
        JTextArea descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        newPanel.add(descScrollPane, gbc);
        
        // Send notification checkbox
        JCheckBox notifyCheckbox = new JCheckBox("Send notification to affected students");
        notifyCheckbox.setSelected(true);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        newPanel.add(notifyCheckbox, gbc);
        
        // Add button
        JButton addButton = new JButton("Add Deadline");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (projectTypeField.getText().isEmpty() || descriptionArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(AdminDashboard.this,
                        "Project type and description are required",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String dateStr = yearComboBox.getSelectedItem() + "-" + 
                    (monthComboBox.getSelectedIndex() + 1) + "-" + 
                    dayComboBox.getSelectedItem();
                
                // Add new deadline to table
                DefaultTableModel model = (DefaultTableModel) deadlinesTable.getModel();
                model.addRow(new Object[]{
                    projectTypeField.getText(),
                    dateStr,
                    descriptionArea.getText(),
                    currentUser.getFullName()
                });
                
                JOptionPane.showMessageDialog(AdminDashboard.this,
                    "Deadline added successfully!" + 
                    (notifyCheckbox.isSelected() ? 
                        "\nNotification sent to affected students." : ""),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Clear form
                projectTypeField.setText("");
                dayComboBox.setSelectedIndex(0);
                monthComboBox.setSelectedIndex(0);
                yearComboBox.setSelectedIndex(0);
                descriptionArea.setText("");
                notifyCheckbox.setSelected(true);
                
                // Switch to current deadlines tab
                tabbedPane.setSelectedIndex(0);
            }
        });
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        newPanel.add(addButton, gbc);
        
        // Add tabs to tabbed pane
        tabbedPane.addTab("Current Deadlines", currentPanel);
        tabbedPane.addTab("Add New Deadline", newPanel);
        
        panel.add(tabbedPane, BorderLayout.CENTER);
        
        // Add action listeners to buttons in current deadlines tab
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = deadlinesTable.getSelectedRow();
                if (selectedRow >= 0) {
                    // Show edit dialog or switch to edit form
                    JOptionPane.showMessageDialog(AdminDashboard.this,
                        "Edit functionality would open a dialog to edit the selected deadline.",
                        "Edit Deadline",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(AdminDashboard.this,
                        "Please select a deadline to edit",
                        "Selection Required",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = deadlinesTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int result = JOptionPane.showConfirmDialog(AdminDashboard.this,
                        "Are you sure you want to delete this deadline?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION);
                        
                    if (result == JOptionPane.YES_OPTION) {
                        // Remove the deadline from the table
                        DefaultTableModel model = (DefaultTableModel) deadlinesTable.getModel();
                        model.removeRow(selectedRow);
                        
                        JOptionPane.showMessageDialog(AdminDashboard.this,
                            "Deadline deleted successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(AdminDashboard.this,
                        "Please select a deadline to delete",
                        "Selection Required",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
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
