package com.slvrvideocallapp.trndvideo.models;

import com.google.gson.annotations.SerializedName;

public class AdvertisementRoot {

    @SerializedName("facebook")
    private Facebook facebook;

    @SerializedName("google")
    private Google google;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private int status;

    public Facebook getFacebook() {
        return facebook;
    }

    public Google getGoogle() {
        return google;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public static class Google {

        @SerializedName("reward")
        private String reward;

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("publisherId")
        private String publisherId;

        @SerializedName("native")
        private String jsonMemberNative;

        @SerializedName("__v")
        private int V;

        @SerializedName("interstitial")
        private String interstitial;

        @SerializedName("show")
        private boolean show;

        @SerializedName("banner")
        private String banner;

        @SerializedName("_id")
        private String id;

        @SerializedName("type")
        private String type;

        @SerializedName("updatedAt")
        private String updatedAt;

        public String getReward() {
            return reward;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getPublisherId() {
            return publisherId;
        }

        public String getJsonMemberNative() {
            return jsonMemberNative;
        }

        public int getV() {
            return V;
        }

        public String getInterstitial() {
            return interstitial;
        }

        public boolean isShow() {
            return show;
        }

        public String getBanner() {
            return banner;
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

    public static class Facebook {

        @SerializedName("reward")
        private String reward;

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("interstitial")
        private String interstitial;

        @SerializedName("__v")
        private int V;

        @SerializedName("show")
        private boolean show;

        @SerializedName("banner")
        private String banner;

        @SerializedName("_id")
        private String id;

        @SerializedName("type")
        private String type;

        @SerializedName("updatedAt")
        private String updatedAt;

        public String getReward() {
            return reward;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getInterstitial() {
            return interstitial;
        }

        public int getV() {
            return V;
        }

        public boolean isShow() {
            return show;
        }

        public String getBanner() {
            return banner;
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
}