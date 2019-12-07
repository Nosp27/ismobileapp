package com.example.ismobileapp.model;

import android.graphics.drawable.Drawable;
import com.example.ismobileapp.network.json.JSONField;
import org.json.JSONException;
import org.json.JSONObject;

public class Region implements Entity {
    @JSONField
    public Integer regionId;
    @JSONField
    public String regionName;

    public Region() {
    }

    public Region(int regionId, String name) {
        this.regionId = regionId;
        this.regionName = name;
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
    public Drawable getImage() {
        return null;
    }

    public static Region createFromJSON(JSONObject regionJSON) {
        if (regionJSON == null || regionJSON == JSONObject.NULL)
            return null;
        try {
            Region region = new Region();
            region.regionId = regionJSON.getInt("id");
            region.regionName = regionJSON.getString("name");
            return region;
        } catch (JSONException e) {
            return null;
        }
    }
}
