package com.university.model;

import java.util.Date;

public class Deadline {
    private int projectId;
    private Date deadlineDate;
    private String description;

    // Modify the constructor to accept only projectId, deadlineDate, and description
    public Deadline(int projectId, Date deadlineDate, String description) {
        this.projectId = projectId;
        this.deadlineDate = deadlineDate;
        this.description = description;
    }

    // Getters and setters for each field
    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
