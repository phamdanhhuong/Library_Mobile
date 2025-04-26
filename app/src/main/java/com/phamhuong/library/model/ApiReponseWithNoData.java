package com.phamhuong.library.model;

import com.google.gson.annotations.SerializedName;

public class ApiReponseWithNoData {
    @SerializedName("success")
    private boolean status; // Changed 'success' to 'status' to match common boolean naming conventions

    @SerializedName("message")
    private String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}