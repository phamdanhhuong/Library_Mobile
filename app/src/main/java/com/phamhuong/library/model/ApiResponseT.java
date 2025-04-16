package com.phamhuong.library.model;

import com.google.gson.annotations.SerializedName;

public class ApiResponseT<T> {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private T data;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}