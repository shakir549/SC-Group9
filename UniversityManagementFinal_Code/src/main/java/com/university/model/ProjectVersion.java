package com.university.model;

import java.sql.Timestamp;

public class ProjectVersion {
    private int versionNumber;
    private String filePath;
    private Timestamp uploadDate;
    private int submissionId;

    public ProjectVersion(int versionNumber, String filePath, Timestamp uploadDate, int submissionId) {
        this.versionNumber = versionNumber;
        this.filePath = filePath;
        this.uploadDate = uploadDate;
        this.submissionId = submissionId;
    }

    // Getters and setters for each field
    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Timestamp getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }
}
