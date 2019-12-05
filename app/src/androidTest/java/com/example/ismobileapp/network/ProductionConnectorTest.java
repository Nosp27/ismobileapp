package com.example.ismobileapp.network;

import android.util.Log;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.ismobileapp.model.Category;
import com.example.ismobileapp.model.Criteries;
import com.example.ismobileapp.model.Facility;
import com.example.ismobileapp.model.Region;
import org.junit.*;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ProductionConnectorTest {
    private static final String TAG = "ProductionConnectorTest";
    private static boolean successfulPing = true;
    private static ProductionConnector connector;

    @BeforeClass
    public static void ping() {
        try {
            connector = new ProductionConnector();
            HttpURLConnection conn =
                    ProductionConnector.setupConnection(
                            new URL(ProductionConnector.SERVER + ProductionConnector.GET_ALL_REGIONS)
                    );
            if (conn.getResponseCode() == 200) {
                successfulPing = true;
                return;
            }
        } catch (IOException e) {
        }
        successfulPing = false;

    }

    @Before
    public void assumePinging() {
        Assume.assumeTrue("Cannot connect to server", successfulPing);
    }

    @Test
    public void testGetRegions() {
        List<Region> actual = connector.getAllRegions();
        Assert.assertNotNull(actual);
    }

    @Test
    public void getAllCategories() {
        List<Category> actual = connector.getAllCategories();
        Assert.assertNotNull(actual);
    }

    @Test
    public void getCriterizedFacilities() {
        List<Facility> actual = connector.getCriterizedFacilities(new Criteries());
        Assert.assertNotNull(actual);
    }
}