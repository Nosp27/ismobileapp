package com.example.ismobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ismobileapp.model.Category;
import com.example.ismobileapp.model.Criteries;
import com.example.ismobileapp.model.Entity;
import com.example.ismobileapp.model.Region;
import com.example.ismobileapp.model.callbacks.EntityListener;
import com.example.ismobileapp.resourceSupplier.ResourceSupplier;
import com.example.ismobileapp.network.Connectors;
import com.example.ismobileapp.network.LoadTask;
import com.example.ismobileapp.viewmodel.EntitySpinnerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String MESSAGE_TAG = "com.example.ismobileapp.MainActivity.MESSAGE";

    ResourceSupplier connector;
    private StoreListener<Region> regionListener;
    private StoreListener<Category> categoryListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connector = Connectors.getDefaultConnector();
        setContentView(R.layout.layout_loading);
        loadRegionsAndCategories();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.layout_loading);
        loadRegionsAndCategories();
    }

    private void onLoad(Pair<List<Region>, List<Category>> data) {
        if (data == null) {
            // exception occurred
            showErrorScreen();
            return;
        }
        setContentView(R.layout.activity_select_criterias);

        regionListener = new StoreListener<>(data.first);
        categoryListener = new StoreListener<>(data.second);
        Spinner listRegions = findViewById(R.id.listRegions);
        listRegions.setAdapter(new EntitySpinnerAdapter(listRegions.getContext(), regionListener));

        Spinner listCategories = findViewById(R.id.listCategories);
        listCategories.setAdapter(new EntitySpinnerAdapter(listCategories.getContext(), categoryListener));

        (findViewById(R.id.btn_select_regions)).setOnClickListener((x) -> selectRegions(formCriteries()));
    }

    private void showErrorScreen() {
        setContentView(R.layout.error_screen);
        Button retryBtn = findViewById(R.id.retry_button);
        retryBtn.setOnClickListener(e -> loadRegionsAndCategories());
    }

    private void loadRegionsAndCategories() {
        LoadTask<Pair<List<Region>, List<Category>>> loadRegionsTask = new LoadTask<>(
                this::loadRegionsAndCategoriesCallback,
                this::onLoad
        );
        loadRegionsTask.execute(new Criteries());
    }

    /**
     * Do not call from main thread
     */
    private Pair<List<Region>, List<Category>> loadRegionsAndCategoriesCallback(Criteries... crits) {
        try {
            return new Pair<>(connector.getAllRegions(), connector.getAllCategories());
        } catch (IOException e) {
            Log.e(TAG, "loadFacilitiesCallback: loadRegionsAndCategoriesCallback", e);
            return null;
        }
    }

    private Criteries formCriteries() {
        Criteries ret = new Criteries();
        ret.regions = regionListener.selectedEntities.stream().map(Region::getRegionId).toArray(Integer[]::new);
        ret.categories = categoryListener.selectedEntities.stream().map(Category::getCatId).toArray(Integer[]::new);
        return ret;
    }

    void selectRegions(Criteries criteries) {
        Intent showFacilitiesIntent = new Intent(this, ActivityInvestingFacilities.class);
        showFacilitiesIntent.putExtra(MESSAGE_TAG, criteries);
        startActivity(showFacilitiesIntent);
    }

    private static class StoreListener<T extends Entity> implements EntityListener<T> {
        private List<T> allEntities;
        private List<T> selectedEntities;

        public StoreListener(List<T> allEntities) {
            this.allEntities = allEntities;
        }

        @Override
        public List<T> getEntities() {
            return allEntities;
        }

        @Override
        public List<T> getSelectedEntities() {
            if(selectedEntities == null)
                selectedEntities = new ArrayList<>();
            return selectedEntities;
        }

        @Override
        public void selectEntity(T entity) {
            if (selectedEntities == null) {
                selectedEntities = new ArrayList<>();
            }
            selectedEntities.add(entity);
        }

        @Override
        public void deselectEntity(T entity) {
            selectedEntities.remove(entity);
        }
    }
}
