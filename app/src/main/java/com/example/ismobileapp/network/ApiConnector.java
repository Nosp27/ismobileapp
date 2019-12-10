package com.example.ismobileapp.network;

import com.example.ismobileapp.model.Category;
import com.example.ismobileapp.model.Criteries;
import com.example.ismobileapp.model.Facility;
import com.example.ismobileapp.model.Region;

import java.io.IOException;
import java.util.List;

public interface ApiConnector {
    List<Region> getAllRegions();
    Object getRegion(int id);
    List<Category> getAllCategories();
    List<Facility> getCriterizedFacilities(Criteries criteries);
    byte[] loadImage(String key) throws IOException;
}
