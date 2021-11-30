
package com.slvrvideocallapp.trndvideo.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class HitAdsRoot {

    @SerializedName("data")
    private Data mData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private Long mStatus;

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
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
    public static class Data {

        @SerializedName("ad_id")
        private String mAdId;
        @SerializedName("country")
        private String mCountry;
        @SerializedName("createdAt")
        private String mCreatedAt;
        @SerializedName("updatedAt")
        private String mUpdatedAt;
        @SerializedName("__v")
        private Long m_V;
        @SerializedName("_id")
        private String m_id;

        public String getAdId() {
            return mAdId;
        }

        public void setAdId(String adId) {
            mAdId = adId;
        }

        public String getCountry() {
            return mCountry;
        }

        public void setCountry(String country) {
            mCountry = country;
        }

        public String getCreatedAt() {
            return mCreatedAt;
        }

        public void setCreatedAt(String createdAt) {
            mCreatedAt = createdAt;
        }

        public String getUpdatedAt() {
            return mUpdatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            mUpdatedAt = updatedAt;
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
