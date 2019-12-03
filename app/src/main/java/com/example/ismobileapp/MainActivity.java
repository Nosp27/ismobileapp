package com.example.ismobileapp;

import android.content.Intent;
import android.util.Pair;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.ismobileapp.model.Category;
import com.example.ismobileapp.model.Criteries;
import com.example.ismobileapp.model.Entity;
import com.example.ismobileapp.model.Region;
import com.example.ismobileapp.model.callbacks.EntityListener;
import com.example.ismobileapp.network.ApiConnector;
import com.example.ismobileapp.network.LoadTask;
import com.example.ismobileapp.network.ProductionConnector;
import com.example.ismobileapp.viewmodel.EntitySpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String MESSAGE_TAG = "com.example.ismobileapp.MainActivity.MESSAGE";

    ApiConnector connector;
    private StoreListener<Region> regionListener;
    private StoreListener<Category> categoryListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connector = new ProductionConnector();
        setContentView(R.layout.layout_loading);
        loadRegionsAndCategories();
    }

    private void onLoad(Pair<List<Region>, List<Category>> data) {
        setContentView(R.layout.activity_select_criterias);

        regionListener = new StoreListener<>(data.first);
        categoryListener = new StoreListener<>(data.second);
        Spinner listRegions = findViewById(R.id.listRegions);
        listRegions.setAdapter(new EntitySpinnerAdapter(listRegions.getContext(), regionListener));

        Spinner listCategories = findViewById(R.id.listCategories);
        listCategories.setAdapter(new EntitySpinnerAdapter(listCategories.getContext(), categoryListener));

        (findViewById(R.id.btn_select_regions)).setOnClickListener((x) -> selectRegions(formCriteries()));
    }

    private void loadRegionsAndCategories() {
        LoadTask<Pair<List<Region>, List<Category>>> loadRegionsTask = new LoadTask<>(
                c -> new Pair<>(connector.getAllRegions(), connector.getAllCategories()),
                this::onLoad
        );
        loadRegionsTask.execute(new Criteries());
    }

    private Criteries formCriteries() {
        Criteries ret = new Criteries();
        ret.regions = regionListener.selectedEntities.stream().map(x->x.regionId).toArray(Integer[]::new);
        ret.categories = categoryListener.selectedEntities.stream().map(Entity::getTitle).toArray(String[]::new);
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
