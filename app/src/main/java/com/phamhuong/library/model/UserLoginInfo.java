package com.phamhuong.library.model;

public class UserLoginInfo {
    public int userId;
    public String username;
    public String password;
    public String fullName;
    public String email;
    public String token;
    private String avatarUrl;
    public String phoneNumber;

    public UserLoginInfo() {
    }

    public UserLoginInfo(int userId, String username, String password, String fullName, String email, String token) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.token = token;
    }
    // Thêm constructor có avatarUrl
    public UserLoginInfo(int userId, String username, String password, String fullName, String email, String token, String avatarUrl, String phoneNumber) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.token = token;
        this.avatarUrl = avatarUrl;
        this.phoneNumber = phoneNumber;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
