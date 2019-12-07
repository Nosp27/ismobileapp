package com.example.ismobileapp.model;

import android.graphics.drawable.Drawable;
import com.example.ismobileapp.network.json.JSONField;

import java.io.Serializable;
import java.util.Arrays;

public class Facility implements Entity, Serializable {
    public String getName() {
        return name;
    }

    @JSONField
    public int _id;

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

    @JSONField
    public Double lat;
    @JSONField
    public Double lng;
    @JSONField
    public String name;
    @JSONField
    public String description;

    public Facility() {
    }

    public Facility(String name, Double[] coords) {
        this.name = name;
        this.lat = coords[0];
        this.lng = coords[1];
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
        return null;
    }
}
