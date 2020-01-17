package com.pashikhmin.ismobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.fragment.app.FragmentActivity;
import com.example.ismobileapp.R;
import com.pashikhmin.ismobileapp.map.FacilityInfoWindowAdapter;
import com.pashikhmin.ismobileapp.map.MapTool;
import com.pashikhmin.ismobileapp.model.Criteries;
import com.pashikhmin.ismobileapp.model.Entity;
import com.pashikhmin.ismobileapp.model.Facility;
import com.pashikhmin.ismobileapp.model.callbacks.EntityListener;
import com.pashikhmin.ismobileapp.resourceSupplier.ResourceSupplier;
import com.pashikhmin.ismobileapp.network.Connectors;
import com.pashikhmin.ismobileapp.network.LoadTask;
import com.pashikhmin.ismobileapp.viewmodel.EntityListAdapter;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityInvestingFacilities extends FragmentActivity {

    private static final String TAG = "ActivityInvestingFacilities";
    public static final String FACILITY_TAG = "com.example.ismobileapp.ActivityInvestingFacilities.FACILITY";

    private MapTool mapTool;
    ResourceSupplier connector = Connectors.getDefaultCachedConnector();
    List<Facility> facilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        mapTool = new MapTool(this);
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
        mapFragment.getMapAsync(map -> mapTool.initializeMap(map, facilities));
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
}
