

package com.university.model;
import java.time.LocalDateTime;

public class Submission {

    private int id;
    private Project project;
    private User student;
    private String filePath;
    private String description;
    private LocalDateTime submissionDate;
    private int version;
    private Status status;
    private byte[] fileData;

    
    private int studentId;
    private int projectId;
    
    private String fileType;
    

    // Enum for submission status
    public enum Status {
        PENDING, APPROVED, REJECTED;
    }


        public Submission() {
        this.project = new Project();  // Initialize the project object
        this.student = new User();     // Initialize the student object
        this.submissionDate = LocalDateTime.now(); // Set current date/time
        this.version = 1;  // Default version
        this.status = Status.PENDING; // Default status
        
    }

  public Submission(int studentId, int projectId, String filePath, String fileType, String description) {
        this.student = new User();  // Create new User object
        this.student.setId(studentId);  // Set student ID
        
        this.project = new Project();  // Create new Project object
        this.project.setId(projectId);  // Set project ID
        
        this.filePath = filePath;
        this.description = description;
        this.submissionDate = LocalDateTime.now();  // Set current date/time
        this.version = 1;  // Default version
        this.status = Status.PENDING;  // Default status
           this.studentId = studentId;
        this.projectId = projectId;
        this.filePath = filePath;
        this.fileType = fileType;
        this.description = description;
    }


    
    // Getters and Setters


       public int getStudentId() {
        return studentId;
    }

    public int getProjectId() {
        return projectId;
    }

 

    public String getFileType() {
        return fileType;
    }

      
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

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}
