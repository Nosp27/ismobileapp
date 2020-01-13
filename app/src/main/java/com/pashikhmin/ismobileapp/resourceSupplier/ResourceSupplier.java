package com.pashikhmin.ismobileapp.resourceSupplier;

import android.graphics.drawable.Drawable;
import com.pashikhmin.ismobileapp.model.Category;
import com.pashikhmin.ismobileapp.model.Criteries;
import com.pashikhmin.ismobileapp.model.Facility;
import com.pashikhmin.ismobileapp.model.Region;

import java.io.IOException;
import java.util.List;

public interface ResourceSupplier {
    List<Region> getAllRegions() throws IOException;
    List<Category> getAllCategories() throws IOException;
    List<Facility> getCriterizedFacilities(Criteries criteries) throws IOException;
    Drawable loadImage(Integer key) throws IOException;
}
