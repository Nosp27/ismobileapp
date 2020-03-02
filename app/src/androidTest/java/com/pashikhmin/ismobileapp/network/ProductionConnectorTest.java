package com.pashikhmin.ismobileapp.network;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.pashikhmin.ismobileapp.model.Category;
import com.pashikhmin.ismobileapp.model.Criteries;
import com.pashikhmin.ismobileapp.model.Facility;
import com.pashikhmin.ismobileapp.model.Region;
import com.pashikhmin.ismobileapp.network.Connectors;
import com.pashikhmin.ismobileapp.network.ProductionConnector;
import com.pashikhmin.ismobileapp.resourceSupplier.CredentialsResourceSupplier;
import com.pashikhmin.ismobileapp.resourceSupplier.ResourceSupplier;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ProductionConnectorTest {
    private static final String TAG = "ProductionConnectorTest";
    private ResourceSupplier connector;

    @Before
    public void assumePing() throws IOException {
//        Assume.assumeTrue("Cannot connect to server", Connectors.pingServer());

        //Stub connector because for ProductionConnector authentication is needed
        connector = new StubConnector();
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

    @Test
    public void fetchCookie() throws IOException {
        CredentialsResourceSupplier crs = new ProductionConnector();
        String cookie = crs.getCookie("dixid96666@winemail.net", "Password123");
        Assert.assertNotNull(cookie);

        boolean thrown = false;
        try {
            crs = new ProductionConnector();
            cookie = crs.getCookie("dixid96666@winemail.net", "SomeWrongPassword");
            Assert.assertNotNull(cookie);
        } catch (IOException e) {
            thrown = true;
        }

        Assert.assertTrue(thrown);
    }
}