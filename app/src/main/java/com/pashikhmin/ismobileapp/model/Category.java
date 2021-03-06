package com.pashikhmin.ismobileapp.model;

import android.graphics.drawable.Drawable;
import com.pashikhmin.ismobileapp.network.connectors.Connectors;
import com.pashikhmin.ismobileapp.network.json.JSONField;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Category implements Entity, Serializable {
    @JSONField
    private Integer catId;
    @JSONField
    private String catName;
    @JSONField
    private Integer imageId;

    private transient Drawable image;

    public Category() {
    }

    public Category(Integer id, String name) {
        catId = id;
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

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        image = Connectors.api().loadImage(imageId);
    }
}
