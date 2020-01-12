package com.example.ismobileapp.model;

import android.graphics.drawable.Drawable;
import com.example.ismobileapp.network.json.JSONField;

import java.io.Serializable;
import java.util.Arrays;

public class Facility implements Entity, Serializable {
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
    @JSONField
    private Integer imageId;
    @JSONField
    private Region region;
    @JSONField
    private Category[] categories;

    private Drawable image;

    public Facility() {
    }

    public Facility(String name, Double[] coords) {
        this.name = name;
        this.lat = coords[0];
        this.lng = coords[1];
        this.imageId = null;
    }

    @Override
    public Integer getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public Region getRegion() {
        return region;
    }

    public Category[] getCategories() {
        return categories;
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
        return image;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
