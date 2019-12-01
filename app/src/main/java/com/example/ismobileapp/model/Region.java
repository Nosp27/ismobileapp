package com.example.ismobileapp.model;

import android.graphics.drawable.Drawable;
import com.example.ismobileapp.network.json.JSONField;
import org.json.JSONException;
import org.json.JSONObject;

public class Region implements Entity {
    @JSONField
    public Integer id;
    @JSONField
    public String name;

    public Region() {
    }

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

    public static Region createFromJSON(JSONObject regionJSON) {
        if (regionJSON == null || regionJSON == JSONObject.NULL)
            return null;
        try {
            Region region = new Region();
            region.id = regionJSON.getInt("id");
            region.name = regionJSON.getString("name");
            return region;
        } catch (JSONException e) {
            return null;
        }
    }
}
