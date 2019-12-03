package com.example.ismobileapp;

import android.content.Intent;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.ismobileapp.model.Category;
import com.example.ismobileapp.model.Criteries;
import com.example.ismobileapp.model.Entity;
import com.example.ismobileapp.model.Region;
import com.example.ismobileapp.model.callbacks.EntityListener;
import com.example.ismobileapp.network.ApiConnector;
import com.example.ismobileapp.network.MockConnector;
import com.example.ismobileapp.viewmodel.EntitySpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String MESSAGE_TAG = "com.example.ismobileapp.MainActivity.MESSAGE";


    ApiConnector connector;
    RegionStoreListener regionListener;
    CategoryStoreListener categoryListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_criterias);

        boolean debugmaps = false;
        if (debugmaps){
            selectRegions(null);
            return;
        }

        connector = new MockConnector();

        regionListener = new RegionStoreListener();
        categoryListener = new CategoryStoreListener();
        Spinner listRegions = findViewById(R.id.listRegions);
        listRegions.setAdapter(new EntitySpinnerAdapter(listRegions.getContext(), regionListener));

        Spinner listCategories = findViewById(R.id.listCategories);
        listCategories.setAdapter(new EntitySpinnerAdapter(listCategories.getContext(), categoryListener));

        (findViewById(R.id.btn_select_regions)).setOnClickListener((x) -> selectRegions(formCriteries()));
    }


    private Criteries formCriteries() {
        Criteries ret = new Criteries();
        ret.regions = regionListener.selectedRegions.stream().map(x->((Region)x).id).toArray(Integer[]::new);
        ret.categories = categoryListener.selectedCategories.stream().map(Entity::getTitle).toArray(String[]::new);
        return ret;
    }

    void selectRegions(Criteries criteries) {
        Intent showFacilitiesIntent = new Intent(this, ActivityInvestingFacilities.class);
        showFacilitiesIntent.putExtra(MESSAGE_TAG, criteries);
        startActivity(showFacilitiesIntent);
    }

    private class RegionStoreListener implements EntityListener {
        private List<Entity> selectedRegions;

        @Override
        public List<Entity> getEntities() {
            return new ArrayList<>(connector.getAllRegions());
        }

        @Override
        public List<Entity> getSelectedEntities() {
            if(selectedRegions == null)
                selectedRegions = new ArrayList<>();
            return selectedRegions;
        }

        @Override
        public void selectEntity(Entity entity) {
            Region region = (Region) entity;
            if (selectedRegions == null) {
                selectedRegions = new ArrayList<>();
            }
            selectedRegions.add(region);
        }

        @Override
        public void deselectEntity(Entity entity) {
            Region region = (Region) entity;
            selectedRegions.remove(region);
        }
    }

    private class CategoryStoreListener implements EntityListener {
        @Override
        public List<Entity> getEntities() {
            return new ArrayList<>(connector.getAllCategories());
        }

        private List<Entity> selectedCategories;

        @Override
        public List<Entity> getSelectedEntities() {
            if(selectedCategories == null)
                selectedCategories = new ArrayList<>();
            return selectedCategories;
        }

        @Override
        public void selectEntity(Entity entity) {
            Category category = ((Category) entity);
            if (selectedCategories == null) {
                selectedCategories = new ArrayList<>();
            }
            selectedCategories.add(category);
        }

        @Override
        public void deselectEntity(Entity entity) {
            Category category = ((Category) entity);
            selectedCategories.remove(category.getTitle());
        }
    }
}
