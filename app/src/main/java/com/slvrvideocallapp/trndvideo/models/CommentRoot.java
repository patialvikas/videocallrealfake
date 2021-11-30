
package com.slvrvideocallapp.trndvideo.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class CommentRoot {

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

        @SerializedName("comment")
        private String mComment;
        @SerializedName("name")
        private String mName;

        public String getComment() {
            return mComment;
        }

        public void setComment(String comment) {
            mComment = comment;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

    }
}
