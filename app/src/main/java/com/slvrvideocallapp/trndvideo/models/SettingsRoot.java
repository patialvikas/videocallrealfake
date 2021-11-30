
package com.slvrvideocallapp.trndvideo.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class SettingsRoot {

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

        @SerializedName("createdAt")
        private String mCreatedAt;
        @SerializedName("loginBonus")
        private Long mLoginBonus;
        @SerializedName("referralBonus")
        private Long mReferralBonus;
        @SerializedName("second")
        private Long mSecond;
        @SerializedName("updatedAt")
        private String mUpdatedAt;
        @SerializedName("videoBonus")
        private Long mVideoBonus;
        @SerializedName("__v")
        private Long m_V;
        @SerializedName("_id")
        private String m_id;

        public String getCreatedAt() {
            return mCreatedAt;
        }

        public void setCreatedAt(String createdAt) {
            mCreatedAt = createdAt;
        }

        public Long getLoginBonus() {
            return mLoginBonus;
        }

        public void setLoginBonus(Long loginBonus) {
            mLoginBonus = loginBonus;
        }

        public Long getReferralBonus() {
            return mReferralBonus;
        }

        public void setReferralBonus(Long referralBonus) {
            mReferralBonus = referralBonus;
        }

        public Long getSecond() {
            return mSecond;
        }

        public void setSecond(Long second) {
            mSecond = second;
        }

        public String getUpdatedAt() {
            return mUpdatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            mUpdatedAt = updatedAt;
        }

        public Long getVideoBonus() {
            return mVideoBonus;
        }

        public void setVideoBonus(Long videoBonus) {
            mVideoBonus = videoBonus;
        }

        public Long get_V() {
            return m_V;
        }

        public void set_V(Long _V) {
            m_V = _V;
        }

        public String get_id() {
            return m_id;
        }

        public void set_id(String _id) {
            m_id = _id;
        }

    }
}
