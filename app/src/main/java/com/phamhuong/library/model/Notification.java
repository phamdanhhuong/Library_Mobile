package com.phamhuong.library.model;

import com.google.gson.annotations.SerializedName;

public class Notification {
    @SerializedName("id")
    private String id;

    @SerializedName("userId")
    private String userId;

    @SerializedName("title")
    private String title;
    
    @SerializedName("message")
    private String message;

    @SerializedName("type")
    private NotificationType type;
    
    @SerializedName("createdAt")
    private String timestamp;
    
    @SerializedName("read")
    private boolean isRead;

    public Notification(String id, String title, String message, NotificationType type, String timestamp) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.type = type;
        this.timestamp = timestamp;
        this.isRead = false;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    }
