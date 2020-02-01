package com.pashikhmin.ismobileapp.network;

import android.graphics.drawable.Drawable;
import com.pashikhmin.ismobileapp.model.Category;
import com.pashikhmin.ismobileapp.model.Criteries;
import com.pashikhmin.ismobileapp.model.Facility;
import com.pashikhmin.ismobileapp.model.Region;
import com.pashikhmin.ismobileapp.resourceSupplier.BinaryDataProvider;
import com.pashikhmin.ismobileapp.resourceSupplier.ResourceSupplier;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class StubConnector implements ResourceSupplier, BinaryDataProvider {

    private BinaryDataProvider binaryDataProvider;

    protected StubConnector() {
        setBinaryDataProvider(this);
    }

    @Override
    public List<Region> getAllRegions() {
        return Arrays.asList(
                new Region(0, "Moscow"),
                new Region(1, "Balashikha"),
                new Region(2, "SPB")
        );
    }


    @Override
    public List<Category> getAllCategories() {
        return Arrays.asList(
                new Category(0, "Property"),
                new Category(1, "IT Industry"),
                new Category(2, "Factories")
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
    public BinaryDataProvider getBinaryDataProvider() {
        return binaryDataProvider;
    }

    @Override
    public void setBinaryDataProvider(BinaryDataProvider provider) {
        this.binaryDataProvider = provider;
    }

    @Override
    public boolean changeLike(Facility facility) throws IOException {
        return true;
    }

    @Override
    public List<Facility> getLikedFacilities() throws IOException {
        return new LinkedList<>();
    }

    @Override
    public Drawable loadImage(Integer key) {
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
        return Drawable.createFromStream(new ByteArrayInputStream(bts), null);
    }
}
