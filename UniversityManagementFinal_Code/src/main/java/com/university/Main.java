package com.university;

import com.university.view.LoginForm;

import javax.swing.*;
import java.awt.*;


/**
 * Main entry point for the University Project Management System.
 * Initializes the application with the login screen.
 */
public class Main {
    public static void main(String[] args) {
        // Set the look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            setUIFont(new javax.swing.plaf.FontUIResource("SansSerif", Font.PLAIN, 14));
        } catch (Exception e) {
            System.err.println("Error setting Look and Feel:");
            e.printStackTrace();
        }

        // Check JDBC driver presence before launching GUI
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found!");
            System.err.println("Please ensure mysql-connector-j-8.x.x.jar is in your classpath.");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "MySQL JDBC Driver not found!\nPlease ensure it's added to your project.",
                "Database Driver Error",
                JOptionPane.ERROR_MESSAGE);
            return; // Don't launch app if DB won't work
        }

        // Launch the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
            System.out.println("🎓 University Project Management System started successfully!");
        });
    }

    /**
     * Sets the default font for all Swing components
     */
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
}
