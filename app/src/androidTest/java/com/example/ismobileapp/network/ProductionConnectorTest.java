package com.example.ismobileapp.network;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.ismobileapp.model.Category;
import com.example.ismobileapp.model.Criteries;
import com.example.ismobileapp.model.Facility;
import com.example.ismobileapp.model.Region;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ProductionConnectorTest {
    private static final String TAG = "ProductionConnectorTest";
    private ProductionConnector connector;

    @Before
    public void assumePing() throws IOException {
        Assume.assumeTrue("Cannot connect to server", Connectors.pingServer());
        connector = new ProductionConnector();
    }

    @Test
    public void testGetRegions() throws IOException {
        List<Region> actual = connector.getAllRegions();
        Assert.assertNotNull(actual);
    }

    @Test
    public void getAllCategories() throws IOException {
        List<Category> actual = connector.getAllCategories();
        Assert.assertNotNull(actual);
    }

    @Test
    public void getCriterizedFacilities() throws IOException {
        List<Facility> actual = connector.getCriterizedFacilities(new Criteries());
        Assert.assertNotNull(actual);
    }
}