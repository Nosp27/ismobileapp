package com.pashikhmin.ismobileapp.model;

import android.graphics.drawable.Drawable;
import com.pashikhmin.ismobileapp.network.connectors.Connectors;
import com.pashikhmin.ismobileapp.network.json.JSONField;

import java.io.*;
import java.util.Arrays;
import java.util.Locale;

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
    @JSONField
    private String utility;
    @JSONField
    private Integer employees;
    @JSONField
    private Double investmentSize;
    @JSONField
    private Double profitability;

    private transient Drawable image;
    private Boolean liked = false;

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

    public String getRegionTitle() {
        if (region == null)
            return "Unknown";
        return getRegion().getTitle();
    }

    public Category[] getCategories() {
        return categories;
    }

    @Override
    public String getTitle() {
        return getName();
    }

    @Override
    public String getSubtitle() {
        return getRegionTitle();
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

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        image = Connectors.api().loadImage(imageId);
    }

    public synchronized Boolean getLiked() {
        return liked;
    }

    public synchronized void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public String getUtility() {
        return utility;
    }

    public Integer getEmployees() {
        return employees;
    }

    public Double getInvestmentSize() {
        return investmentSize;
    }

    public Double getProfitability() {
        return profitability;
    }

    public String getInvestmentSizeStr() {
        return String.format(Locale.getDefault(), "$%.1f", investmentSize);
    }
}
