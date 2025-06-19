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
 * Registration form for the University Project Management System.
 * Allows new users to register with the system.
 * Features responsive design for various screen sizes.
 */
public class RegisterForm extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;
    private final JTextField fullNameField;
    private final JTextField emailField;
    private final JComboBox<Role> roleComboBox;
    private final JButton registerButton;
    private final JButton cancelButton;
    private final JFrame parentFrame;
    private final JPanel mainPanel;
    
    // Minimum dimensions for the form
    private static final int MIN_WIDTH = 500;
    private static final int MIN_HEIGHT = 450;
    
    public RegisterForm(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        
        // Set up the frame
        setTitle("University Project Management System - Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setSize(600, 500); // Larger initial size
        setLocationRelativeTo(null); // Center on screen
        
        // Create the main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create a panel for the form content
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weightx = 1.0; // Allow horizontal stretching
        
        // Title label with responsive font size
        JLabel titleLabel = new JLabel("Register New User", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
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
        
        usernameField = new JTextField(20);
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
        
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(passwordField, gbc);
        
        // Confirm Password label and field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:", SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(confirmPasswordLabel, gbc);
        
        confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(confirmPasswordField, gbc);
        
        // Full Name label and field
        JLabel fullNameLabel = new JLabel("Full Name:", SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(fullNameLabel, gbc);
        
        fullNameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(fullNameField, gbc);
        
        // Email label and field
        JLabel emailLabel = new JLabel("Email:", SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(emailLabel, gbc);
        
        emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(emailField, gbc);
        
        // Role label and dropdown
        JLabel roleLabel = new JLabel("Role:", SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(roleLabel, gbc);
        
        roleComboBox = new JComboBox<>(Role.values());
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(roleComboBox, gbc);
        
        // Panel for buttons with FlowLayout for better spacing
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        // Register button
        registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(120, 35));
        buttonPanel.add(registerButton);
        
        // Cancel button
        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(120, 35));
        buttonPanel.add(cancelButton);
        
        // Add button panel to the grid
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);
        
        // Add form panel to the main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Create a footer panel with helpful text
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel footerLabel = new JLabel("All fields are required for registration");
        footerLabel.setForeground(Color.GRAY);
        footerPanel.add(footerLabel);
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
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
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
        
        if (width < 500) {
            titleFont = new Font("Arial", Font.BOLD, 16);
            regularFont = new Font("Arial", Font.PLAIN, 12);
        } else if (width < 700) {
            titleFont = new Font("Arial", Font.BOLD, 20);
            regularFont = new Font("Arial", Font.PLAIN, 14);
        } else {
            titleFont = new Font("Arial", Font.BOLD, 24);
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
            if (label.getText().equals("Register New User")) {
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
     * Handles the user registration process.
     */
    private void registerUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        Role selectedRole = (Role) roleComboBox.getSelectedItem();
        
        // Validate input
        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "All fields are required", 
                "Registration Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Passwords do not match", 
                "Registration Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Basic email validation
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, 
                "Invalid email format", 
                "Registration Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Attempt to register user
        UserController userController = UserController.getInstance();
        User newUser = userController.register(username, password, fullName, email, selectedRole);
        
        if (newUser != null) {
            // Registration successful
            JOptionPane.showMessageDialog(this, 
                "Registration successful. You can now login.", 
                "Registration Success", 
                JOptionPane.INFORMATION_MESSAGE);
            this.dispose(); // Close registration form
            parentFrame.setVisible(true); // Show login form again
        } else {
            // Registration failed
            JOptionPane.showMessageDialog(this, 
                "Username already exists. Please choose a different username.", 
                "Registration Failed", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Cancels the registration and returns to login form.
     */
    private void cancel() {
        this.dispose(); // Close registration form
        parentFrame.setVisible(true); // Show login form again
    }
}
