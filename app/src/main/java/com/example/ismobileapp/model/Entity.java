package com.example.ismobileapp.model;

import android.graphics.drawable.Drawable;

public interface Entity {
    Integer getId();
    String getTitle();
    String getSubtitle();
    Drawable getImage();
    Integer getImageId();
    void setImage(Drawable image);
}
