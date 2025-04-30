package com.phamhuong.library.model;


import com.google.gson.annotations.SerializedName;

public class AvailableTotalBooks {
    @SerializedName("totalBooks")
    private int totalBooks;
    @SerializedName("availableBooks")
    private int availableBooks;

    public int getTotalBooks() {
        return totalBooks;
    }

    public int getAvailableBooks() {
        return availableBooks;
    }
}