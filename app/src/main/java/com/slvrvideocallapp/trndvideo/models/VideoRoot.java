
package com.slvrvideocallapp.trndvideo.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class VideoRoot {

    @Expose
    private List<Datum> data;
    @Expose
    private Video video;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    @SuppressWarnings("unused")
    public static class Datum {

        @Expose
        private String message;
        @Expose
        private String name;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    @SuppressWarnings("unused")
    public static class Video {

        @SerializedName("__v")
        private Long _V;
        @Expose
        private String _id;
        @Expose
        private String country;
        @Expose
        private String createdAt;
        @Expose
        private String updatedAt;
        @Expose
        private String video;

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

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

    }
}
