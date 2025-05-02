package com.phamhuong.library.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("userId")
    private int userId;

    @SerializedName("username")
    private String username;

    @SerializedName("fullName")
    private String fullName;
    @SerializedName("avatarUrl")
    private String avatar;

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getAvatar() {
        return avatar;
    }
}
