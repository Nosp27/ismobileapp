package com.example.ismobileapp;

import android.content.Intent;
import android.widget.GridView;
import android.widget.TabHost;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.example.ismobileapp.model.Criteries;
import com.example.ismobileapp.model.Entity;
import com.example.ismobileapp.model.Facility;
import com.example.ismobileapp.model.callbacks.EntityListener;
import com.example.ismobileapp.network.ApiConnector;
import com.example.ismobileapp.network.LoadTask;
import com.example.ismobileapp.network.MockConnector;
import com.example.ismobileapp.viewmodel.EntityListAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class ActivityInvestingFacilities extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "ActivityInvestingFacilities";
    public static final String FACILITY_TAG = "com.example.ismobileapp.ActivityInvestingFacilities.FACILITY";

    private GoogleMap mMap;
    ApiConnector connector = new MockConnector();
    List<Facility> facilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);
        loadFacilities();
    }

    void onLoadFacilities(List<Facility> loadedFacilities) {
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
                new LoadTask<>(connector::getCriterizedFacilities, this::onLoadFacilities);
        Intent intent = getIntent();
        Criteries criteries = (Criteries) intent.getSerializableExtra(MainActivity.MESSAGE_TAG);
        facilityLoadTask.execute(criteries);
    }

    void addMarkersForFacilities() {
        for (Facility f : facilities) {
            double[] coords = f.getCoords();
            LatLng position = new LatLng(coords[0], coords[1]);
            mMap.addMarker(new MarkerOptions().position(position).title(f.getName()));
        }
    }

    void initFacilities() {
        GridView grid = findViewById(R.id.viewFacilities);
        grid.setAdapter(
                new EntityListAdapter(grid.getContext(), R.layout.facility_list_item, new EntityListener() {
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
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
