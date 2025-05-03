package com.phamhuong.library.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Category implements Serializable {
    @SerializedName("genreId")
    private int id;
    @SerializedName("genre")
    private String genre;
    @SerializedName("url")
    private String images;
    @SerializedName("bookCount") // Thêm dòng này để ánh xạ với JSON
    private int bookCount;
    @SerializedName("ebookCount")
    private int ebookCount;
    @SerializedName("audiobookCount")
    private int audiobookCount;

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookCount() {
        return bookCount;
    }
    public int getEBookCount() {
        return ebookCount;
    }
    public int getAudioBookCount() {
        return audiobookCount;
    }
    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
