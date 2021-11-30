package com.slvrvideocallapp.trndvideo.realvideocallapp.models;

import java.io.Serializable;

public class CatItem implements Serializable {
    public String itemId, itemName, itemIv, videoUrl,videoEnable;

    private int viewType = 0;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemIv() {
        return itemIv;
    }

    public void setItemIv(String itemIv) {
        this.itemIv = itemIv;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoEnable() {
        return videoEnable;
    }

    public void setVideoEnable(String videoEnable) {
        this.videoEnable = videoEnable;
    }

    public int getViewType() {
        return viewType;
    }

    public CatItem setViewType(int viewType) {
        this.viewType = viewType;
        return this;
    }
}
