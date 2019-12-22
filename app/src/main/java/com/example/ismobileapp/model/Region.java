package com.example.ismobileapp.model;

import android.graphics.drawable.Drawable;
import com.example.ismobileapp.cache.DrawablesCache;
import com.example.ismobileapp.network.json.JSONField;

import java.io.Serializable;

public class Region implements Entity, Serializable {
    @JSONField
    private Integer regionId;
    @JSONField
    private String regionName;
    @JSONField(processResultMethod = "processImage")
    private String imageUrl;

    public Region() {
    }

    public Region(int regionId, String name) {
        this.regionId = regionId;
        this.regionName = name;
        this.imageUrl = "";
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    //package-private accessor, only for testing for now
    String getImageUrl() {
        return imageUrl;
    }

    //package-private accessor, only for testing for now
    void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String getTitle() {
        return regionName;
    }

    @Override
    public String getSubtitle() {
        return "";
    }

    @Override
    public Drawable getImage() {
        return DrawablesCache.processImage(imageUrl, regionName + "_region_image");
    }

    public void processImage() {
        DrawablesCache.processImage(imageUrl, regionName + "_region_image");
    }
}
