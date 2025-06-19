package com.university.model;

import java.util.logging.Logger;

/**
 * Represents a user in the system.
 * Contains user information like ID, username, password, full name, email, and role.
 */
public class User {
    private static final Logger LOGGER = Logger.getLogger(User.class.getName());

    private int id;
    private String username;
    private String password; // Consider hashing before storing
    private String fullName;
    private String email;
    private Role role;
    private String projectName;
    private String supervisorName;
    private String status;
private Project project; 
    // Default constructor
    public User() {}

    // Constructor with basic fields
    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Full constructor with validation
    public User(int id, String username, String password, String fullName, String email, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.project = null; 
        validateUser();
    }

    // Validation method to ensure required fields are set
    private void validateUser() {
        if (username == null || password == null || role == null) {
            LOGGER.warning("⚠️ User object is missing required fields!");
        }
    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
 public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        LOGGER.warning("⚠️ Accessing password field directly. Consider using hashing.");
        return password;
    }

    public void setPassword(String password) {
        this.password = password; // Consider using a hashing method here
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}