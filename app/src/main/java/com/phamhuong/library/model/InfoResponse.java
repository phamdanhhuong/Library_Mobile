package com.phamhuong.library.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InfoResponse implements Serializable {
    @SerializedName("full_name")
    String full_name;
    @SerializedName("id")
    int id;
    @SerializedName("email")
    String email;
    @SerializedName("avatar")
    String avatar;



    @SerializedName("phone_number")
    String phone_number;

    public InfoResponse(String full_name, int id, String email, String avatar, String phone_number) {
        this.full_name = full_name;
        this.id = id;
        this.email = email;
        this.avatar = avatar;
        this.phone_number = phone_number;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
