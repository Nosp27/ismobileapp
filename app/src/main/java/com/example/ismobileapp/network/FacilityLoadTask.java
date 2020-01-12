package com.example.ismobileapp.network;

import android.os.AsyncTask;
import com.example.ismobileapp.model.Criteries;
import com.example.ismobileapp.model.Facility;
import com.example.ismobileapp.resourceSupplier.ResourceSupplier;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class FacilityLoadTask extends AsyncTask<Criteries, Integer, List<Facility>> {
    private ResourceSupplier connector;
    private Consumer<List<Facility>> callback;

    public FacilityLoadTask(ResourceSupplier connector, Consumer<List<Facility>> callback) {
        this.connector = connector;
        this.callback = callback;
    }

    @Override
    protected List<Facility> doInBackground(Criteries... criteries) {
        try {
            return connector.getCriterizedFacilities(criteries[0]);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Facility> facilities) {
        callback.accept(facilities);
    }
};
