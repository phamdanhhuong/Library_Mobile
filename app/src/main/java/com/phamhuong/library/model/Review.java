package com.phamhuong.library.model;

import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("reviewId")
    private int reviewId;

    @SerializedName("user")
    private User user;

    @SerializedName("bookId")
    private int bookId;

    @SerializedName("rating")
    private float rating;

    @SerializedName("comment")
    private String comment;

    @SerializedName("createdAt")
    private String createdAt;

    public int getReviewId() { return reviewId; }
    public User getUser() { return user; }  // Thay vì getUserId() và getUserName()
    public int getBookId() { return bookId; }
    public float getRating() { return rating; }
    public String getComment() { return comment; }

    public String getUserName() {
        return user != null ? user.getUsername() : "Unknown";
    }
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
