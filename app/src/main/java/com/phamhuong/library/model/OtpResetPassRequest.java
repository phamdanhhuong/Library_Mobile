package com.phamhuong.library.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OtpResetPassRequest implements Serializable {
    @SerializedName("email")
    private String email;
    public OtpResetPassRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
