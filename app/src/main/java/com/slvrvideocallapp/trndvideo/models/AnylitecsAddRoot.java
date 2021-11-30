package com.slvrvideocallapp.trndvideo.models;

import com.google.gson.annotations.SerializedName;

public class AnylitecsAddRoot {

    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private Userdata user;

    @SerializedName("status")
    private int status;

    public String getMessage() {
        return message;
    }

    public Userdata getUser() {
        return user;
    }

    public int getStatus() {
        return status;
    }
}