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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class ActivityInvestingFacilities extends FragmentActivity implements HeaderFragmentRequred {
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
        loadFacilities(this::onPrimaryLoadFacilities);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (facilities != null)
            loadFacilities(this::onRefineFacilities);
    }

    private void showErrorScreen() {
        setContentView(R.layout.error_screen);
        Button retryBtn = findViewById(R.id.retry_button);

        // TODO: validate
        retryBtn.setOnClickListener(e -> loadFacilities(this::onPrimaryLoadFacilities));
    }

    void onPrimaryLoadFacilities(LoadTaskResult<List<Facility>> res) {
        if (!res.successful()) {
            showErrorScreen();
            return;
        }

        setContentView(R.layout.activity_investing_facilities);
        this.facilities = res.getResult();
        initFacilities();

        setupTabHost();
        setTitle("Investment Opportunities");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(map -> mapTool.initializeMap(map, facilities, this::loadFacilityOnInfoWindowClick));
    }

    void onRefineFacilities(LoadTaskResult<List<Facility>> res) {
        if (!res.successful()) {
            showErrorScreen();
            return;
        }

        this.facilities = res.getResult();
        initFacilities();
    }

    private void setupTabHost() {
        TabHost tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec regionTab = tabHost.newTabSpec(opportunitiesTabTag());
        regionTab.setContent(R.id.tabWithFacilitiesTable);
        regionTab.setIndicator("", getResources().getDrawable(R.drawable.invest_ops_selector, getTheme()));
        tabHost.addTab(regionTab);

        TabHost.TabSpec mapTab = tabHost.newTabSpec(mapTabTag());
        mapTab.setContent(R.id.tabWithMap);
        mapTab.setIndicator("", getResources().getDrawable(R.drawable.map_globe_selector, getTheme()));
        tabHost.addTab(mapTab);

        for (int i = 0; i < tabHost.getChildCount(); i++)
            ((TextView) tabHost.getTabWidget().getChildTabViewAt(i).findViewById(android.R.id.title)).setTextColor(
                    getResources().getColor(R.color.colorShade, getTheme())
            );

        tabHost.setCurrentTab(0);
    }

    void loadFacilities(Consumer<LoadTaskResult<List<Facility>>> afterDone) {
        LoadTask<List<Facility>> facilityLoadTask =
                new LoadTask<>(this::loadFacilitiesCallback, afterDone);
        Intent intent = getIntent();
        Criteries criteries = (Criteries) intent.getSerializableExtra(MainActivity.CRITERIAS_TAG);
        facilityLoadTask.execute(criteries);
    }

    private List<Facility> loadFacilitiesCallback(Criteries... criterias) {
        try {
            List<Facility> facilities = connector.getCriterizedFacilities(criterias[0]);

            if (Connectors.userAuthorized()) {
                Set<Integer> likedFacilitiesIds = likedFacilityIdSet(connector.getLikedFacilities());
                for (Facility f : facilities)
                    f.setLiked(likedFacilitiesIds.contains(f.getId()));
            }
            return facilities;
        } catch (IOException e) {
            Log.e(TAG, "loadFacilitiesCallback: loadFacilitiesCallback", e);
            throw new RuntimeException(e);
        }
    }

    private Set<Integer> likedFacilityIdSet(List<Facility> likedFacilities) {
        Set<Integer> ret = new HashSet<>();
        for (Facility f : likedFacilities)
            ret.add(f.getId());
        return ret;
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
                return opportunitiesTabTag();
            case "map_layout_header":
                return mapTabTag();
            default:
                throw new RuntimeException("Unknown header fragment");
        }
    }

    private String opportunitiesTabTag() {
        if(getIntent().hasExtra("custom_header"))
            return getIntent().getStringExtra("custom_header");
        return getResources().getString(R.string.opportunities);
    }

    private String mapTabTag() {
        return getResources().getString(R.string.map);
    }
}
