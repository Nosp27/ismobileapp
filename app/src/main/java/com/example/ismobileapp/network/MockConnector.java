package com.example.ismobileapp.network;

import com.example.ismobileapp.model.Category;
import com.example.ismobileapp.model.Criteries;
import com.example.ismobileapp.model.Facility;
import com.example.ismobileapp.model.Region;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class MockConnector implements ApiConnector {

    @Override
    public List<Region> getAllRegions() {
        return Arrays.asList(
                new Region(1, "Moscow"),
                new Region(2, "Balashikha"),
                new Region(3, "SPB")
        );
    }

    @Override
    public Object getRegion(int id) {
        return null;
    }

    @Override
    public List<Category> getAllCategories() {
        return Arrays.asList(
                new Category("Property"),
                new Category("IT Industry"),
                new Category("Factories")
        );
    }

    @Override
    public List<Facility> getCriterizedFacilities(Criteries criteries) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(
                new Facility("Hse", new Double[]{55.23, 34.33}),
                new Facility("SAS", new Double[]{15.11, 24.32})
        );
    }

    @Override
    public InputStream loadImage(String key) {
        byte[] bts = new byte[]{
                -119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0,
                13, 73, 72, 68, 82, 0, 0, 0, 1, 0,
                0, 0, 1, 1, 3, 0, 0, 0, 37, -37,
                86, -54, 0, 0, 0, 3, 80, 76, 84, 69,
                -1, 77, 0, 92, 53, 56, 127, 0, 0, 0,
                1, 116, 82, 78, 83, -52, -46, 52, 86, -3,
                0, 0, 0, 10, 73, 68, 65, 84, 120, -100,
                99, 98, 0, 0, 0, 6, 0, 3, 54, 55,
                124, -88, 0, 0, 0, 0, 73, 69, 78, 68, -82,
        };
        return null;
    }
}
