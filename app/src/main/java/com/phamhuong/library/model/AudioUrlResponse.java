package com.phamhuong.library.model;

import com.google.gson.annotations.SerializedName;
public class AudioUrlResponse {
    @SerializedName("audioUrl")
    private String audioUrl;

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
}
