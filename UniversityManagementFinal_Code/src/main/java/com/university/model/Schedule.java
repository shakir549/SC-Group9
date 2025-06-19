package com.university.model;
import java.util.Date;

public class Schedule {
    private int scheduleId;
    private int projectId;
    private String taskName;
    private Date startDate;
    private Date endDate;

    public Schedule(int projectId, String taskName, Date startDate, Date endDate) {
        this.projectId = projectId;
        this.taskName = taskName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }
    public int getProjectId() { return projectId; }
    public String getTaskName() { return taskName; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
}