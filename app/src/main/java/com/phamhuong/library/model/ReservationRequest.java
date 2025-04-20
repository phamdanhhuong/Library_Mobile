package com.phamhuong.library.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationRequest {
    @SerializedName("userId")
    private Integer userId;
    @SerializedName("bookIds")
    private List<Integer> bookIds;
    @SerializedName("expirationDate")
    private String expirationDate;
    @SerializedName("status")
    private String status;

    public ReservationRequest(){}
    public ReservationRequest(Integer userId, List<Integer> bookIds, String expirationDate) {
        this.userId = userId;
        this.bookIds = bookIds;
        this.expirationDate = expirationDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Integer> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<Integer> bookIds) {
        this.bookIds = bookIds;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
