package com.slvrvideocallapp.trndvideo.models;

import com.google.gson.annotations.SerializedName;

public class Userdata {

    @SerializedName("country")
    private String country;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("__v")
    private int V;

    @SerializedName("_id")
    private String id;

    @SerializedName("type")
    private String type;

    @SerializedName("updatedAt")
    private String updatedAt;

    public String getCountry() {
        return country;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public int getV() {
        return V;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}