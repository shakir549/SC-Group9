package com.university.model;

import java.sql.Timestamp;

public class Notification {
    private int notificationId;
    private int senderId;
    private int recipientId;
    private String subject;
    private String message;
    private Timestamp sendDate;
    private String importance;
    private String isRead;


    // Default constructor
 public Notification() {
    }

    // Constructor
    public Notification(int senderId, int recipientId, String subject, String message, Timestamp sendDate, String importance, String isRead) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.subject = subject;
        this.message = message;
        this.sendDate = sendDate;
        this.importance = importance;
        this.isRead = isRead;
    }

    // Getters and setters
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getSendDate() {
        return sendDate;
    }

    public void setSendDate(Timestamp sendDate) {
        this.sendDate = sendDate;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}
