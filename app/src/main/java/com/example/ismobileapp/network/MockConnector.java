package com.example.ismobileapp.network;

import com.example.ismobileapp.model.Category;
import com.example.ismobileapp.model.Criteries;
import com.example.ismobileapp.model.Facility;
import com.example.ismobileapp.model.Region;

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
    public byte[] loadImage(String key) {
        return "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABAQMAAAAl21bKAAAAA1BMVEX/TQBcNTh/AAAAAXRSTlPM0jRW/QAAAApJREFUeJxjYgAAAAYAAzY3fKgAAAAASUVORK5CYII="
                .getBytes();
    }
}
