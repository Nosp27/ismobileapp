package com.example.ismobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TabHost;
import androidx.fragment.app.FragmentActivity;
import com.example.ismobileapp.model.Criteries;
import com.example.ismobileapp.model.Entity;
import com.example.ismobileapp.model.Facility;
import com.example.ismobileapp.model.callbacks.EntityListener;
import com.example.ismobileapp.network.ApiConnector;
import com.example.ismobileapp.network.Connectors;
import com.example.ismobileapp.network.LoadTask;
import com.example.ismobileapp.viewmodel.EntityListAdapter;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityInvestingFacilities extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "ActivityInvestingFacilities";
    public static final String FACILITY_TAG = "com.example.ismobileapp.ActivityInvestingFacilities.FACILITY";

    private GoogleMap mMap;
    ApiConnector connector = Connectors.getDefaultConnector();
    List<Facility> facilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        loadFacilities();
    }

    private void showErrorScreen() {
        setContentView(R.layout.error_screen);
        Button retryBtn = findViewById(R.id.retry_button);
        retryBtn.setOnClickListener(e -> loadFacilities());
    }

    void onLoadFacilities(List<Facility> loadedFacilities) {
        if (loadedFacilities == null) {
            showErrorScreen();
            return;
        }
        setContentView(R.layout.activity_investing_facilities);
        this.facilities = loadedFacilities;
        initFacilities();

        setTitle("Investment Opportunities");
        TabHost tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec regionTab = tabHost.newTabSpec("tab1");
        regionTab.setContent(R.id.tabWithFacilitiesTable);
        regionTab.setIndicator("Opportunities");
        tabHost.addTab(regionTab);

        TabHost.TabSpec mapTab = tabHost.newTabSpec("tab2");
        mapTab.setContent(R.id.tabWithMap);
        mapTab.setIndicator("Map");
        tabHost.addTab(mapTab);

        tabHost.setCurrentTab(0);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    void loadFacilities() {
        LoadTask<List<Facility>> facilityLoadTask =
                new LoadTask<>(this::loadFacilitiesCallback, this::onLoadFacilities);
        Intent intent = getIntent();
        Criteries criteries = (Criteries) intent.getSerializableExtra(MainActivity.MESSAGE_TAG);
        facilityLoadTask.execute(criteries);
    }

    private List<Facility> loadFacilitiesCallback(Criteries... criterias) {
        try {
            return connector.getCriterizedFacilities(criterias[0]);
        } catch (IOException e) {
            Log.e(TAG, "loadFacilitiesCallback: loadFacilitiesCallback", e);
            return null;
        }
    }

    void addMarkersForFacilities() {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (Facility f : facilities) {
            if (f.getLat() == null && f.getLng() == null)
                continue;
            LatLng position = new LatLng(f.getLat(), f.getLng());
            builder.include(position);
            mMap.addMarker(new MarkerOptions().position(position).title(f.getName()));
        }

        CameraUpdate camUpdate;
        if (facilities.size() > 1)
            camUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), 500, 900, 10);
        else if (!facilities.isEmpty()){
            Facility facility = facilities.get(0);
            camUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(facility.getLat(), facility.getLng()), 10f);
        }
        else {
            return;
        }
        mMap.moveCamera(camUpdate);
    }

    void initFacilities() {
        GridView grid = findViewById(R.id.viewFacilities);
        grid.setAdapter(
                new EntityListAdapter(grid.getContext(), R.layout.list_item_facility, new EntityListener() {
                    @Override
                    public List<Entity> getEntities() {
                        return new ArrayList<>(facilities);
                    }

                    @Override
                    public List<Entity> getSelectedEntities() {
                        return null;
                    }

                    @Override
                    public void selectEntity(Entity entity) {
                        detailFacility(entity);
                    }

                    @Override
                    public void deselectEntity(Entity entity) {

                    }
                }));
    }

    private void detailFacility(Entity selected) {
        Facility f = (Facility) selected;

        Intent intent = new Intent(this, FacilityDetailed.class);
        intent.putExtra(FACILITY_TAG, f);
        startActivity(intent);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        addMarkersForFacilities();
    }
}
