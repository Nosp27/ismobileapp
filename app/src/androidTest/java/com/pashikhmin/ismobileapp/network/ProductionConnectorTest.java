package com.pashikhmin.ismobileapp.network;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.pashikhmin.ismobileapp.model.Category;
import com.pashikhmin.ismobileapp.model.Criteries;
import com.pashikhmin.ismobileapp.model.Facility;
import com.pashikhmin.ismobileapp.model.Region;
import com.pashikhmin.ismobileapp.network.connectors.Connectors;
import com.pashikhmin.ismobileapp.resourceSupplier.ApiConnector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ProductionConnectorTest {
    private static final String TAG = "ProductionConnectorTest";
    private ApiConnector connector;

    @Before
    public void assumePing() throws IOException {
//        Assume.assumeTrue("Cannot connect to server", Connectors.pingServer());

        //Stub connector because for ProductionConnector authentication is needed
        connector = Connectors.createStub();
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