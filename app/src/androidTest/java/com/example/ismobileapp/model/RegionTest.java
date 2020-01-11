package com.example.ismobileapp.model;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.ismobileapp.network.Connectors;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class RegionTest {
    @Test
    public void testLoadImageInProduction() {
        Assume.assumeTrue(Connectors.pingServer());
        Connectors.setDebugMode(false);
        Region region = new Region(1, "test region");
        region.setImageId(1);
        region.processImage();
        Assert.assertNotNull(region.getImage());
    }

    @Test
    public void testLoadImageInDebug() {
        Connectors.setDebugMode(true);
        Region region = new Region(1, "test region");
        region.setImageId(1);
        region.processImage();
        Assert.assertNotNull(region.getImage());
    }
}