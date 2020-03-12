package com.pashikhmin.ismobileapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.model.Marker;
import com.pashikhmin.ismobileapp.map.MapTool;
import com.pashikhmin.ismobileapp.model.Criteries;
import com.pashikhmin.ismobileapp.model.Entity;
import com.pashikhmin.ismobileapp.model.Facility;
import com.pashikhmin.ismobileapp.model.callbacks.EntityListener;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTaskResult;
import com.pashikhmin.ismobileapp.resourceSupplier.ApiConnector;
import com.pashikhmin.ismobileapp.network.connectors.Connectors;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTask;
import com.pashikhmin.ismobileapp.viewmodel.EntityListAdapter;
import com.google.android.gms.maps.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityInvestingFacilities extends FragmentActivity implements HeaderFragmentRequred {
    private final String opportunitiesTabTag = "opportunities";
    private final String mapTabTag = "map";

    private String tabTag = opportunitiesTabTag;
    private static final int DETAILED_FACILITY_REQUEST_CODE = 288;
    private static final String TAG = "ActivityInvestingFacilities";
    static final String FACILITY_TAG = "com.pashikhmin.ismobileapp.ActivityInvestingFacilities.FACILITY";

    private MapTool mapTool;
    ApiConnector connector;
    List<Facility> facilities;

    Facility selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connector = Connectors.api();
        setContentView(R.layout.layout_loading);
        mapTool = new MapTool(this);
        loadFacilities();
    }

    private void showErrorScreen() {
        setContentView(R.layout.error_screen);
        Button retryBtn = findViewById(R.id.retry_button);
        retryBtn.setOnClickListener(e -> loadFacilities());
    }

    void onLoadFacilities(LoadTaskResult<List<Facility>> res) {
        if (!res.successful()) {
            showErrorScreen();
            return;
        }

        List<Facility> loadedFacilities = res.getResult();
        setContentView(R.layout.activity_investing_facilities);
        this.facilities = loadedFacilities;
        initFacilities();

        setupTabHost();
        setTitle("Investment Opportunities");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(map -> mapTool.initializeMap(map, facilities, this::loadFacilityOnInfoWindowClick));
    }

    private void setupTabHost() {
        TabHost tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec regionTab = tabHost.newTabSpec(opportunitiesTabTag);
        regionTab.setContent(R.id.tabWithFacilitiesTable);
        regionTab.setIndicator("", getResources().getDrawable(R.drawable.invest_ops_selector, getTheme()));
        tabHost.addTab(regionTab);

        TabHost.TabSpec mapTab = tabHost.newTabSpec(mapTabTag);
        mapTab.setContent(R.id.tabWithMap);
        mapTab.setIndicator("", getResources().getDrawable(R.drawable.map_globe_selector, getTheme()));
        tabHost.addTab(mapTab);

        for (int i = 0; i < tabHost.getChildCount(); i++)
            ((TextView) tabHost.getTabWidget().getChildTabViewAt(i).findViewById(android.R.id.title)).setTextColor(
                    getResources().getColor(R.color.colorShade, getTheme())
            );

        tabHost.setOnTabChangedListener(e -> tabTag = e);
        tabHost.setCurrentTab(0);
        tabTag = tabHost.getCurrentTabTag();
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
            throw new RuntimeException(e);
        }
    }


    void initFacilities() {
        ListView facilityContainer = findViewById(R.id.viewFacilities);
        facilityContainer.setAdapter(
                new EntityListAdapter(facilityContainer.getContext(), R.layout.list_item_facility, new EntityListener() {
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

    private void loadFacilityOnInfoWindowClick(Marker marker) {
        detailFacility(((Facility) marker.getTag()));
    }

    private void detailFacility(Entity selected) {
        this.selected = (Facility) selected;

        Intent intent = new Intent(this, FacilityDetailed.class);
        intent.putExtra(FACILITY_TAG, this.selected);
        startActivityForResult(intent, DETAILED_FACILITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (
                requestCode == DETAILED_FACILITY_REQUEST_CODE &&
                        resultCode == Activity.RESULT_OK &&
                        data != null &&
                        data.getExtras() != null &&
                        data.getExtras().containsKey("liked")
        ) {
            Boolean liked = data.getBooleanExtra("liked", false);
            selected.setLiked(liked);
            initFacilities();
        }
    }

    @Override
    public int resourceId(String tag) {
        return R.layout.header_fragment;
    }

    @Override
    public String topic(String tag) {
        switch (tag) {
            case "facility_list_layout_header":
                return opportunitiesTabTag;
            case "map_layout_header":
                return mapTabTag;
            default:
                throw new RuntimeException("Unknown header fragment");
        }
    }
}
