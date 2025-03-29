package com.phamhuong.library.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegisterRequest implements Serializable {
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("full_name")
    private String full_name;
    @SerializedName("phone_number")
    private Long phone_number;
    @SerializedName("email")
    private String email;

    public RegisterRequest(String username, String password, String full_name, Long phone_number, String email) {
        this.username = username;
        this.password = password;
        this.full_name = full_name;
        this.phone_number = phone_number;
        this.email = email;
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

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public Long getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(Long phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
