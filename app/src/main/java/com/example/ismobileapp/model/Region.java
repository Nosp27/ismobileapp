package com.example.ismobileapp.model;

import android.graphics.drawable.Drawable;
import com.example.ismobileapp.cache.DrawablesCache;
import com.example.ismobileapp.network.json.JSONField;

import java.io.Serializable;

public class Region implements Entity, Serializable {
    @JSONField
    public Integer regionId;
    @JSONField
    public String regionName;
    @JSONField(processResultMethod = "processImage")
    String imageUrl;

    public Region() {
    }

    public Region(int regionId, String name) {
        this.regionId = regionId;
        this.regionName = name;
        this.imageUrl = "";
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
