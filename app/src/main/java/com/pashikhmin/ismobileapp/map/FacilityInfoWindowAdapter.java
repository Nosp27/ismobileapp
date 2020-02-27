package com.pashikhmin.ismobileapp.map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.pashikhmin.ismobileapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.pashikhmin.ismobileapp.model.Facility;

public class FacilityInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Activity activity;

    public FacilityInfoWindowAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        Facility associatedFacility = ((Facility) marker.getTag());
        View ret = activity.getLayoutInflater()
                .inflate(R.layout.layout_facility_map_popup, null, false);
        ((TextView) ret.findViewById(R.id.title)).setText(associatedFacility.getName());
        ((TextView) ret.findViewById(R.id.facility_region)).setText(associatedFacility.getRegionTitle());
        if (associatedFacility.getImage() != null)
            ((ImageView) ret.findViewById(R.id.entity_image)).setImageDrawable(associatedFacility.getImage());
        return ret;
    }
}
