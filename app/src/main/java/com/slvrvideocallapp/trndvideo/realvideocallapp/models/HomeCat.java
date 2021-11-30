package com.slvrvideocallapp.trndvideo.realvideocallapp.models;

public class HomeCat {
    public String itemId, countryName, itemDesc, itemImage;

    private int viewType = 0;

    public HomeCat() {
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }



    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public int getViewType() {
        return viewType;
    }

    public HomeCat setViewType(int viewType) {
        this.viewType = viewType;
        return this;
    }
}
