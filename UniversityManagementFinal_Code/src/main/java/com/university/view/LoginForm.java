package com.university.view;

import com.university.controller.UserController;
import com.university.model.Role;
import com.university.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;



/**
 * Login form for the University Project Management System.
 * Allows users to login with their username, password, and role.
 * Includes responsive design to adapt to different screen sizes.
 */
public class LoginForm extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JComboBox<Role> roleComboBox;
    private final JButton loginButton;
    private final JButton registerButton;
    private final JPanel mainPanel;
    
    // Minimum dimensions for the form
    private static final int MIN_WIDTH = 400;
    private static final int MIN_HEIGHT = 300;
    
    public LoginForm() {
        // Set up the frame
        setTitle("University Project Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setSize(500, 400); // Larger initial size
        setLocationRelativeTo(null); // Center on screen
        
        // Create the main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create a panel for the form content with grid layout
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weightx = 1.0; // Allow horizontal stretching
        
        // Title label with responsive font size
        JLabel titleLabel = new JLabel("University Project Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(titleLabel, gbc);
        
        // Username label and field
        JLabel usernameLabel = new JLabel("Username:", SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(usernameLabel, gbc);
        
        usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(usernameField, gbc);
        
        // Password label and field
        JLabel passwordLabel = new JLabel("Password:", SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(passwordLabel, gbc);
        
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(passwordField, gbc);
        
        // Role label and dropdown
        JLabel roleLabel = new JLabel("Role:", SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(roleLabel, gbc);
        
        roleComboBox = new JComboBox<>(Role.values());
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(roleComboBox, gbc);
        
        // Panel for buttons with FlowLayout for better spacing
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        // Login button
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(loginButton);
        
        // Register button
        registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(registerButton);
        
        // Add button panel to the grid
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);
        
        // Add form panel to the main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Create a footer panel with version info
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel versionLabel = new JLabel("Version 1.0");
        versionLabel.setForeground(Color.GRAY);
        footerPanel.add(versionLabel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        getContentPane().add(mainPanel);
        
        // Add window resize listener to adjust component sizes
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateFontSizes();
            }
        });
        
        // Add action listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
    }
    
    /**
     * Updates the font sizes based on the current window size
     */
    private void updateFontSizes() {
        int width = getWidth();
        
        Font titleFont;
        Font regularFont;
        
        if (width < 400) {
            titleFont = new Font("Arial", Font.BOLD, 14);
            regularFont = new Font("Arial", Font.PLAIN, 12);
        } else if (width < 600) {
            titleFont = new Font("Arial", Font.BOLD, 18);
            regularFont = new Font("Arial", Font.PLAIN, 14);
        } else {
            titleFont = new Font("Arial", Font.BOLD, 22);
            regularFont = new Font("Arial", Font.PLAIN, 16);
        }
        
        // Update fonts for all components
        for (Component comp : mainPanel.getComponents()) {
            updateComponentFonts(comp, titleFont, regularFont);
        }
        
        revalidate();
    }
    
    
    /**
     * Recursively updates fonts for a component and its children
     */
    private void updateComponentFonts(Component comp, Font titleFont, Font regularFont) {
        if (comp instanceof JLabel) {
            JLabel label = (JLabel) comp;
            if (label.getText().contains("University Project Management System")) {
                label.setFont(titleFont);
            } else {
                label.setFont(regularFont);
            }
        } else if (comp instanceof JTextField || comp instanceof JComboBox || comp instanceof JButton) {
            comp.setFont(regularFont);
        } else if (comp instanceof Container) {
            // Recursively update child components
            for (Component child : ((Container) comp).getComponents()) {
                updateComponentFonts(child, titleFont, regularFont);
            }
        }
    }
    
    /**
     * Handles the login process.
     */
    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        Role selectedRole = (Role) roleComboBox.getSelectedItem();
        
        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Username and password cannot be empty", 
                "Login Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Attempt to login
        UserController userController = UserController.getInstance();
        User user = userController.login(username, password, selectedRole);
        
        if (user != null) {
            // Login successful, open appropriate dashboard
            this.dispose(); // Close login form
            
            switch (user.getRole()) {
                case STUDENT:
                    StudentDashboard studentDashboard = new StudentDashboard(user);
                    studentDashboard.setVisible(true);
                    break;
                case SUPERVISOR:
                    SupervisorDashboard supervisorDashboard = new SupervisorDashboard(user);
                    supervisorDashboard.setVisible(true);
                    break;
                case ADMIN:
                    AdminDashboard adminDashboard = new AdminDashboard(user);
                    adminDashboard.setVisible(true);
                    break;
            }
        } else {
            // Login failed
            JOptionPane.showMessageDialog(this, 
                "Invalid username, password, or role combination", 
                "Login Failed", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Opens the registration form.
     */
    private void register() {
        RegisterForm registerForm = new RegisterForm(this);
        registerForm.setVisible(true);
        this.setVisible(false); // Hide login form while registering
    }


    
}
