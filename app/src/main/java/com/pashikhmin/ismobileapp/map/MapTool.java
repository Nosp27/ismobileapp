package com.pashikhmin.ismobileapp.map;

import android.app.Activity;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.pashikhmin.ismobileapp.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pashikhmin.ismobileapp.model.Facility;

import java.util.List;

public class MapTool implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    private GoogleMap mMap;
    private Activity activity;
    private LinearLayout mapLayout;

    public LinearLayout getMapLayout() {
        if (mapLayout == null)
            mapLayout = activity.findViewById(R.id.tabWithMap);
        return mapLayout;
    }

    public MapTool(Activity activity) {
        this.activity = activity;
    }

    public void initializeMap(GoogleMap googleMap, List<Facility> facilities) {
        mMap = googleMap;
        addMarkersForFacilities(facilities);
        mMap.setInfoWindowAdapter(new FacilityInfoWindowAdapter(activity));
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
    }

    void addMarkersForFacilities(List<Facility> facilities) {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (Facility f : facilities) {
            if (f.getLat() == null && f.getLng() == null)
                continue;
            LatLng position = new LatLng(f.getLat(), f.getLng());
            builder.include(position);
            Marker addedMarker = mMap.addMarker(new MarkerOptions().position(position).title(f.getName()));
            addedMarker.setTag(f);
        }

        CameraUpdate camUpdate;
        if (facilities.size() > 1)
            camUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), 500, 900, 10);
        else if (!facilities.isEmpty()) {
            Facility facility = facilities.get(0);
            camUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(facility.getLat(), facility.getLng()), 10f);
        } else {
            return;
        }
        mMap.moveCamera(camUpdate);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Facility attachedFacility = (Facility) marker.getTag();
        if (attachedFacility == null) {
            throw new IllegalArgumentException("Got Marker without attached facility");
        }

        LinearLayout _mapLayout = getMapLayout();
        removeBriefPanel();
        View facility_brief_view = getBriefView(attachedFacility, _mapLayout);
        _mapLayout.addView(facility_brief_view);
        return false;
    }

    private View getBriefView(Facility facility, ViewGroup parent) {
        View facility_brief_view = activity.getLayoutInflater().inflate(
                R.layout.layout_facility_brief, parent, false
        );
        ((TextView) facility_brief_view.findViewById(R.id.title)).setText(facility.getName());
        ((TextView) facility_brief_view.findViewById(R.id.facility_region)).setText(
                facility.getRegion().getTitle()
        );
        return facility_brief_view;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        removeBriefPanel();
    }

    private void removeBriefPanel() {
        LinearLayout _mapLayout = getMapLayout();
        if (_mapLayout.getChildCount() > 1)
            _mapLayout.removeView(getMapLayout().findViewById(R.id.facility_brief));
    }
}
