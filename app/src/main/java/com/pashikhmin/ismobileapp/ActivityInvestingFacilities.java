package com.pashikhmin.ismobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.pashikhmin.ismobileapp.R;
import com.pashikhmin.ismobileapp.map.MapTool;
import com.pashikhmin.ismobileapp.model.Criteries;
import com.pashikhmin.ismobileapp.model.Entity;
import com.pashikhmin.ismobileapp.model.Facility;
import com.pashikhmin.ismobileapp.model.callbacks.EntityListener;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTaskResult;
import com.pashikhmin.ismobileapp.network.loadTask.SubmitLikesTask;
import com.pashikhmin.ismobileapp.resourceSupplier.ResourceSupplier;
import com.pashikhmin.ismobileapp.network.Connectors;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTask;
import com.pashikhmin.ismobileapp.viewmodel.EntityListAdapter;
import com.google.android.gms.maps.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ActivityInvestingFacilities extends FragmentActivity {
    private static final int DETAILED_FACILITY_REQUEST_CODE = 288;
    private static final String TAG = "ActivityInvestingFacilities";
    public static final String FACILITY_TAG = "com.pashikhmin.ismobileapp.ActivityInvestingFacilities.FACILITY";

    // just for testing
    Lock facilityLoadLock = new ReentrantLock();

    private MapTool mapTool;
    ResourceSupplier connector = Connectors.getDefaultCachedConnector();
    List<Facility> facilities;

    Facility selected;

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

    void onLoadFacilities(LoadTaskResult<List<Facility>> res) {
        try {
            if (!res.successful()) {
                showErrorScreen();
                return;
            }

            List<Facility> loadedFacilities = res.getResult();
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
        } finally {
            facilityLoadLock.unlock();
        }
    }

    void loadFacilities() {
        facilityLoadLock.lock();
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
        this.selected = (Facility) selected;

        Intent intent = new Intent(this, FacilityDetailed.class);
        intent.putExtra(FACILITY_TAG, this.selected);
        startActivityForResult(intent, DETAILED_FACILITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DETAILED_FACILITY_REQUEST_CODE && data != null &&
                data.getExtras() != null &&
                data.getExtras().containsKey("liked")) {
            Boolean liked = data.getBooleanExtra("liked", false);
            selected.setLiked(liked);
            initFacilities();
        }
    }
}
