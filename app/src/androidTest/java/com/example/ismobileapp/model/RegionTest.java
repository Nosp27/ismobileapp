package com.example.ismobileapp.model;

import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.ismobileapp.network.ApiConnector;
import com.example.ismobileapp.network.Connectors;
import com.example.ismobileapp.network.MockConnector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

@RunWith(AndroidJUnit4.class)
public class RegionTest {
    ApiConnector connector;

    @Before
    public void init() {
        connector = new MockConnector();
    }

    @Test
    public void testLoadImage() {
        Region region = new Region(1, "test region");
        region.imageUrl = "";
        region.processImage();
        Assert.assertNotNull(region.image);
    }
}