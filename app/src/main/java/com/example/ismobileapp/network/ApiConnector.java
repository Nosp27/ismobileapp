package com.example.ismobileapp.network;

import com.example.ismobileapp.model.Category;
import com.example.ismobileapp.model.Criteries;
import com.example.ismobileapp.model.Facility;
import com.example.ismobileapp.model.Region;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ApiConnector {
    List<Region> getAllRegions() throws IOException;
    Object getRegion(int id) throws IOException;
    List<Category> getAllCategories() throws IOException;
    List<Facility> getCriterizedFacilities(Criteries criteries) throws IOException;
    InputStream loadImage(String key) throws IOException;
}
