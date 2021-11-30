
package com.slvrvideocallapp.trndvideo.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class OwnAdsRoot {

    @Expose
    private List<Datum> data;
    @Expose
    private String message;
    @Expose
    private Long status;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @SuppressWarnings("unused")
    public static class Datum {

        @SerializedName("__v")
        private Long _V;
        @Expose
        private String _id;
        @Expose
        private String banner;
        @Expose
        private String createdAt;
        @Expose
        private String interstitial;
        @Expose
        private String reward;
        @Expose
        private Boolean show;
        @Expose
        private String thumbnail;
        @Expose
        private String type;
        @Expose
        private String updatedAt;
        @Expose
        private String website;

        public Long get_V() {
            return _V;
        }

        public void set_V(Long _V) {
            this._V = _V;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getBanner() {
            return banner;
        }

        public void setBanner(String banner) {
            this.banner = banner;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getInterstitial() {
            return interstitial;
        }

        public void setInterstitial(String interstitial) {
            this.interstitial = interstitial;
        }

        public String getReward() {
            return reward;
        }

        public void setReward(String reward) {
            this.reward = reward;
        }

        public Boolean getShow() {
            return show;
        }

        public void setShow(Boolean show) {
            this.show = show;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

    }
}
