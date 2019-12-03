package com.example.ismobileapp.network;

import android.os.AsyncTask;
import com.example.ismobileapp.model.Criteries;
import com.example.ismobileapp.model.Facility;

import java.util.List;
import java.util.function.Consumer;

public class FacilityLoadTask extends AsyncTask<Criteries, Integer, List<Facility>> {
    private ApiConnector connector;
    private Consumer<List<Facility>> callback;

    public FacilityLoadTask(ApiConnector connector, Consumer<List<Facility>> callback) {
        this.connector = connector;
        this.callback = callback;
    }

    @Override
    protected List<Facility> doInBackground(Criteries... criteries) {
        return connector.getCriterizedFacilities(criteries[0]);
    }

    @Override
    protected void onPostExecute(List<Facility> facilities) {
        callback.accept(facilities);
    }
};
