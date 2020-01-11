package com.example.ismobileapp.model;

import android.graphics.drawable.Drawable;
import com.example.ismobileapp.cache.DrawablesCache;
import com.example.ismobileapp.network.json.JSONField;

import java.io.Serializable;

public class Category implements Entity, Serializable {
    @JSONField
    Integer catId;
    @JSONField
    String catName;
    @JSONField(processResultMethod = "processImage")
    Integer imageId;

    public Category() {
    }

    public Category(String name) {
        this.catName = name;
        this.imageId = null;
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
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
    public Drawable getImage() {
        return DrawablesCache.processImage(imageId, catName + "_category_image");
    }

    public void processImage() {
        DrawablesCache.processImage(imageId, catName + "_category_image");
    }
}
