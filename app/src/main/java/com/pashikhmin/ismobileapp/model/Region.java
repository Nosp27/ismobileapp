package com.pashikhmin.ismobileapp.model;

import android.graphics.drawable.Drawable;
import com.pashikhmin.ismobileapp.network.Connectors;
import com.pashikhmin.ismobileapp.network.json.JSONField;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Region implements Entity, Serializable {
    @JSONField
    private Integer regionId;
    @JSONField
    private String regionName;
    @JSONField
    private Integer imageId;
    @JSONField
    private Double area;
    @JSONField
    private Integer population;
    @JSONField
    private Integer unemployed;
    @JSONField
    private Integer totalLabourForce;
    @JSONField
    private Double gdp;
    @JSONField
    private Double avgPropertyPrice;
    @JSONField
    private Double avgFamilyIncome;

    private transient Drawable image;

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

    public Integer getImageId() {
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
    public void setImage(Drawable image) {
        this.image = image;
    }

    @Override
    public Drawable getImage() {
        return image;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        image = Connectors.getDefaultCachedConnector().getBinaryDataProvider().loadImage(imageId);
    }
}
