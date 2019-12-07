package com.example.ismobileapp.model;

import android.graphics.drawable.Drawable;
import com.example.ismobileapp.network.json.JSONField;

import java.io.Serializable;
import java.util.Arrays;

public class Facility implements Entity, Serializable {
    public double[] getCoords() {
        return coords;
    }

    public String getName() {
        return name;
    }

    @JSONField
    private int _id;
    @JSONField
    private double[] coords;
    @JSONField
    private String name;
    @JSONField
    private String description;

    public Facility(){}

    public Facility(String name, double[] coords){
        this.name = name;
        this.coords = coords;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSubtitle() {
        return Arrays.toString(coords);
    }

    @Override
    public Drawable getImage() {
        return null;
    }
}
