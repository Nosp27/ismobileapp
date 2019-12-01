package com.example.ismobileapp.model;

import android.graphics.drawable.Drawable;

public class Region implements Entity {
    public int id;
    public String name;

    public Region(int id, String name) {
        this.id = id;
        this.name = name;
    }


    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSubtitle() {
        return "";
    }

    @Override
    public Drawable getImage() {
        return null;
    }
}
