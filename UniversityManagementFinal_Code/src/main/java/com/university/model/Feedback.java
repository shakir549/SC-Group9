package com.university.model;

import java.util.Date;

/**
 * Represents feedback on a submission in the system.
 */
public class Feedback {
    private int id;
    private Submission submission;
    private User supervisor;
    private String comments;
    private int grade; // 0-100
    private Date feedbackDate;
    
    // Default constructor
    public Feedback() {
    }
    
    // Constructor with basic fields
    public Feedback(Submission submission, User supervisor, String comments, int grade) {
        this.submission = submission;
        this.supervisor = supervisor;
        this.comments = comments;
        this.grade = grade;
        this.feedbackDate = new Date();
    }
    
    // Full constructor
    public Feedback(int id, Submission submission, User supervisor, 
                    String comments, int grade, Date feedbackDate) {
        this.id = id;
        this.submission = submission;
        this.supervisor = supervisor;
        this.comments = comments;
        this.grade = grade;
        this.feedbackDate = feedbackDate;
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Submission getSubmission() {
        return submission;
    }
    
    public void setSubmission(Submission submission) {
        this.submission = submission;
    }
    
    public User getSupervisor() {
        return supervisor;
    }
    
    public void setSupervisor(User supervisor) {
        this.supervisor = supervisor;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public int getGrade() {
        return grade;
    }
    
    public void setGrade(int grade) {
        this.grade = grade;
    }
    
    public Date getFeedbackDate() {
        return feedbackDate;
    }
    
    public void setFeedbackDate(Date feedbackDate) {
        this.feedbackDate = feedbackDate;
    }
    
    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", submission=" + (submission != null ? submission.getId() : "none") +
                ", supervisor=" + (supervisor != null ? supervisor.getUsername() : "none") +
                ", grade=" + grade +
                ", feedbackDate=" + feedbackDate +
                '}';
    }
}
