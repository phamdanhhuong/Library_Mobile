package com.phamhuong.library.model;

public class ReviewRequest {
    private int userId;
    private int bookId;
    private float rating;
    private String comment;

    public ReviewRequest(int userId, int bookId, float rating, String comment) {
        this.userId = userId;
        this.bookId = bookId;
        this.rating = rating;
        this.comment = comment;
    }
}
