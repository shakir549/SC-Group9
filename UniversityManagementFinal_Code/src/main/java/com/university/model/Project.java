package com.university.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.university.controller.UserController;

/**
 * Represents a project in the system.
 * Contains project information and relationships to students and supervisors.
 */
public class Project {
    private int id;
    private String title; // Project Title
    private String description; // Project Description
    private String supervisorName; // Supervisor's name
    private List<String> studentNames; // List of student names assigned to the project
    private Date deadline; // Project Deadline
    private String status; // Project status ("Pending", "In Progress", "Completed", "Rejected")
    
    // Default constructor
    public Project() {
        this.studentNames = new ArrayList<>();
    }
    
    // Constructor with basic fields (updated to accept List<String> for studentNames)
    public Project(String title, String description, String supervisorName, List<String> studentNames, Date deadline) {
        this.title = title;
        this.description = description;
        this.supervisorName = supervisorName;  // Initialize supervisor name
        this.studentNames = studentNames;      // Initialize student names as a List<String>
        this.deadline = deadline;
        this.status = "Pending"; // Set default status
    }

    // Constructor for a project with an id and status
    public Project(int id, String title, String description, String supervisorName, Date deadline, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.supervisorName = supervisorName;
        this.deadline = deadline;
        this.status = status;
        this.studentNames = new ArrayList<>();
    }

    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() { // Returns the project title
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public List<String> getStudentNames() {
        return studentNames;
    }

    public void setStudentNames(List<String> studentNames) {
        this.studentNames = studentNames;
    }

    // Add a student's name to the project
    public void addStudentName(String studentName) {
        if (!studentNames.contains(studentName)) {
            studentNames.add(studentName);
        }
    }

    // Remove a student's name from the project
    public void removeStudentName(String studentName) {
        studentNames.remove(studentName);
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    

    // Add this method to return the supervisor from a database query or user lookup
    public User getSupervisor() {
        // Fetch the supervisor by their username using UserController
        return UserController.getInstance().getUserByUsername(supervisorName);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", supervisorName='" + supervisorName + '\'' +
                ", studentNames=" + studentNames.size() +
                ", deadline=" + deadline +
                ", status='" + status + '\'' +
                '}';
    }
}
