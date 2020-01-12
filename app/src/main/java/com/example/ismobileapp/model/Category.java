package com.example.ismobileapp.model;

import android.graphics.drawable.Drawable;
import com.example.ismobileapp.network.json.JSONField;

import java.io.Serializable;

public class Category implements Entity, Serializable {
    @JSONField
    private Integer catId;
    @JSONField
    private String catName;
    @JSONField
    private Integer imageId;

    Drawable image;

    public Category() {
    }

    public Category(String name) {
        this.catName = name;
        this.imageId = null;
    }

    @Override
    public Integer getId() {
        return getCatId();
    }

    public Integer getCatId() {
        return catId;
    }

    @Override
    public String toString() {
        return catName;
    }

    @Override
    public String getTitle() {
        return catName;
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
    public Integer getImageId() {
        return imageId;
    }

    @Override
    public Drawable getImage() {
        return image;
    }
}
