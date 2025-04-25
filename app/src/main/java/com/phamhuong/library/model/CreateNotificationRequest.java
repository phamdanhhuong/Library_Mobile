package com.phamhuong.library.model;

public class CreateNotificationRequest {
    private String userId;
    private String title;
    private String message;
    private String type;
    private String timestamp;

    public CreateNotificationRequest() {
        // Constructor mặc định không tham số.  Quan trọng cho việc sử dụng với một số thư viện JSON.
    }

    public CreateNotificationRequest(String userId, String title, String message, String type, String timestamp) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}