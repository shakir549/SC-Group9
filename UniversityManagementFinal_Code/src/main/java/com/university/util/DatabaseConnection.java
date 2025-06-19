package com.university.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for creating and managing database connections.
 */
public class DatabaseConnection {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/university_project_management";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static volatile DatabaseConnection instance;
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

    private Connection connection;

    // Load JDBC driver once during class initialization
    static {
        try {
            Class.forName(JDBC_DRIVER);
            LOGGER.info("✅ JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "❌ JDBC Driver not found!", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    private DatabaseConnection() {}

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                connection.setAutoCommit(true);
                LOGGER.info("✅ Connected to the database successfully!");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "❌ Failed to establish database connection!", e);
                throw e;
            }
        }
        return connection;
    }

    public static Connection fetchConnection() throws SQLException {
        return getInstance().getConnection();
    }

    public void closeConnection() {
        if (Objects.nonNull(connection)) {
            try {
                connection.close();
                LOGGER.info("✅ Database connection closed.");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "❌ Error closing database connection!", e);
            }
        }
    }

    public boolean isConnectionValid() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Connection validation failed!", e);
            return false;
        }
    }
}