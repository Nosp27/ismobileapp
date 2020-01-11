package com.pashikhmin.ismobileapp.map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import com.example.ismobileapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class FacilityInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Activity activity;

    public FacilityInfoWindowAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return activity.getLayoutInflater().inflate(R.layout.layout_facility_brief, null, false);
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
