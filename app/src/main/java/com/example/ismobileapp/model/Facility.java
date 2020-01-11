package com.example.ismobileapp.model;

import android.graphics.drawable.Drawable;
import com.example.ismobileapp.cache.DrawablesCache;
import com.example.ismobileapp.network.json.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Facility implements Entity, Serializable {
    public String getName() {
        return name;
    }

    @JSONField
    private int _id;

    @JSONField
    private Double lat;
    @JSONField
    private Double lng;
    @JSONField
    private String name;
    @JSONField
    private String description;
    @JSONField(processResultMethod = "processImage")
    private Integer imageId;
    @JSONField
    private Region region;
    @JSONField
    private Category[] categories;

    public Facility() {
    }

    public Facility(String name, Double[] coords) {
        this.name = name;
        this.lat = coords[0];
        this.lng = coords[1];
        this.imageId = null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Category[] getCategories() {
        return categories;
    }

    public void setCategories(Category[] categories) {
        this.categories = categories;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSubtitle() {
        return Arrays.toString(new Double[]{lat, lng});
    }

    @Override
    public Drawable getImage() {
        return DrawablesCache.processImage(imageId, _id + "_facility_image");
    }

    public void processImage() {
        DrawablesCache.processImage(imageId, _id + "_facility_image");
    }
}
