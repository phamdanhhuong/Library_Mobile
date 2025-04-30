package com.phamhuong.library.model;

import com.google.gson.annotations.SerializedName;

public class PopularGenre {
    @SerializedName("genre")
    private String genre;
    @SerializedName("count")
    private int count;

    public String getGenre() {
        return genre;
    }

    public int getCount() {
        return count;
    }
}