package com.phamhuong.library.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class BorrowingRecord {
    @SerializedName("recordId")
    private Integer recordId;

    @SerializedName("user")
    private User user; // Phải có class Users

    @SerializedName("book")
    private Book book;  // Phải có class Book

    @SerializedName("borrowDate")
    private String borrowDate;

    @SerializedName("dueDate")
    private String dueDate;

    @SerializedName("returnDate")
    private String returnDate;

    @SerializedName("status")
    private String status;

    // Getters và Setters

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
