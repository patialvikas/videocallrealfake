
package com.slvrvideocallapp.trndvideo.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class ThumbRoot {

    @SerializedName("data")
    private List<Datum> mData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private Long mStatus;

    public List<Datum> getData() {
        return mData;
    }

    public void setData(List<Datum> data) {
        mData = data;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

    @SuppressWarnings("unused")
    public static class Datum {

        @SerializedName("image")
        private String mImage;
        @SerializedName("name")
        private String mName;

        public String getImage() {
            return mImage;
        }

        public void setImage(String image) {
            mImage = image;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

    }
}
