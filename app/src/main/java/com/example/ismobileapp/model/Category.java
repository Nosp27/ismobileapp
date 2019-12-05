package com.example.ismobileapp.model;

import android.graphics.drawable.Drawable;

public class Category implements Entity {
    private String name;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
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
