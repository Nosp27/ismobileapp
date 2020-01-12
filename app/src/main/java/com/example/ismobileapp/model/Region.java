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
    private Integer imageId;

    public Region() {
    }

    public Region(int regionId, String name) {
        this.regionId = regionId;
        this.regionName = name;
        this.imageId = null;
    }

    @Override
    public Integer getId() {
        return getRegionId();
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
    Integer getImageId() {
        return imageId;
    }

    //package-private accessor, only for testing for now
    void setImageId(Integer imageId) {
        this.imageId = imageId;
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
        return DrawablesCache.processImage(imageId, regionName + "_region_image");
    }

    public void processImage() {
        DrawablesCache.processImage(imageId, regionName + "_region_image");
    }
}
