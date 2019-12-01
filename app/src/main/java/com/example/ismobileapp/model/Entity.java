package com.example.ismobileapp.model;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import org.json.JSONObject;

public interface Entity {
    String getTitle();
    String getSubtitle();
    Drawable getImage();
}
