package com.example.ismobileapp;

import android.content.Intent;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.ismobileapp.model.Category;
import com.example.ismobileapp.model.Criteries;
import com.example.ismobileapp.model.Entity;
import com.example.ismobileapp.model.Region;
import com.example.ismobileapp.model.callbacks.EntityListener;
import com.example.ismobileapp.network.ApiConnector;
import com.example.ismobileapp.network.MockConnector;
import com.example.ismobileapp.viewmodel.EntityAdapter;

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

        boolean debugmaps = true;
        if (debugmaps){
            selectRegions(null);
            return;
        }

        connector = new MockConnector();

        regionListener = new RegionStoreListener();
        categoryListener = new CategoryStoreListener();
        ListView listRegions = findViewById(R.id.listRegions);
        listRegions.setAdapter(new EntityAdapter(listRegions.getContext(), regionListener));

        ListView listCategories = findViewById(R.id.listCategories);
        listCategories.setAdapter(new EntityAdapter(listCategories.getContext(), categoryListener));

        (findViewById(R.id.btn_select_regions)).setOnClickListener((x) -> selectRegions(formCriteries()));
    }


    private Criteries formCriteries() {
        Criteries ret = new Criteries();
        ret.regions = regionListener.selectedRegions.toArray(new Integer[0]);
        ret.categories = categoryListener.selectedCategories.toArray(new String[0]);
        return ret;
    }

    void selectRegions(Criteries criteries) {
        Intent showFacilitiesIntent = new Intent(this, ActivityInvestingFacilities.class);
        showFacilitiesIntent.putExtra(MESSAGE_TAG, criteries);
        startActivity(showFacilitiesIntent);
    }

    private class RegionStoreListener implements EntityListener {
        private List<Integer> selectedRegions;

        @Override
        public List<Entity> getEntities() {
            return new ArrayList<>(connector.getAllRegions());
        }

        @Override
        public void selectEntity(Entity entity) {
            Region region = (Region) entity;
            if (selectedRegions == null) {
                selectedRegions = new ArrayList<>();
            }
            selectedRegions.add(region.id);
        }

        @Override
        public void deselectEntity(Entity entity) {
            Region region = (Region) entity;
            selectedRegions.remove(region.id);
        }
    }

    private class CategoryStoreListener implements EntityListener {
        @Override
        public List<Entity> getEntities() {
            return new ArrayList<>(connector.getAllCategories());
        }

        private List<String> selectedCategories;

        @Override
        public void selectEntity(Entity entity) {
            Category category = ((Category) entity);
            if (selectedCategories == null) {
                selectedCategories = new ArrayList<>();
            }
            selectedCategories.add(category.getTitle());
        }

        @Override
        public void deselectEntity(Entity entity) {
            Category category = ((Category) entity);
            selectedCategories.remove(category.getTitle());
        }
    }
}
