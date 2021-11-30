package com.slvrvideocallapp.trndvideo.models;

import com.google.gson.annotations.SerializedName;

public class AnylitecsRemoveRoot {

    @SerializedName("userwer")
    private Userwer userwer;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private int status;

    public Userwer getUserwer() {
        return userwer;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}