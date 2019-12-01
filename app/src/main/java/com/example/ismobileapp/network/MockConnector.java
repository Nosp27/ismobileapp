package com.example.ismobileapp.network;

import com.example.ismobileapp.model.Category;
import com.example.ismobileapp.model.Criteries;
import com.example.ismobileapp.model.Facility;
import com.example.ismobileapp.model.Region;

import java.util.Arrays;
import java.util.List;

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
        return Arrays.asList(
                new Facility("Hse", new double[]{55.23, 34.33}),
                new Facility("SAS", new double[]{15.11, 24.32})
        );
    }
}
