package com.example.ismobileapp.resourceSupplier;

import android.graphics.drawable.Drawable;
import com.example.ismobileapp.model.Category;
import com.example.ismobileapp.model.Criteries;
import com.example.ismobileapp.model.Facility;
import com.example.ismobileapp.model.Region;

import java.io.IOException;
import java.util.List;

public interface ResourceSupplier {
    List<Region> getAllRegions() throws IOException;
    List<Category> getAllCategories() throws IOException;
    List<Facility> getCriterizedFacilities(Criteries criteries) throws IOException;
    Drawable loadImage(Integer key) throws IOException;
}
