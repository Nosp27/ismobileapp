package com.pashikhmin.ismobileapp.cache;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.pashikhmin.ismobileapp.network.connectors.ConnectorBuilder;
import com.pashikhmin.ismobileapp.network.connectors.HttpConnector;
import com.pashikhmin.ismobileapp.network.connectors.RESTBuilder;
import com.pashikhmin.ismobileapp.network.connectors.Redirect;
import com.pashikhmin.ismobileapp.network.json.JSONModeller;
import com.pashikhmin.ismobileapp.resourceSupplier.ApiConnector;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class ApiCacheTest {
    @Test
    public void testCachingPipeline() throws IOException {
        StubHttpConnector stubHttpConnector = new StubHttpConnector();
        ApiConnector stubConnector = new ConnectorBuilder()
                .rest(new RESTCacheImpl(new RESTBuilder()
                        .wrap(stubHttpConnector)
                        .build()))
                .parser(new JSONModeller())
                .build();
        stubHttpConnector.alert = false;
        stubConnector.getAllRegions();
        stubHttpConnector.alert = true;
        stubConnector.getAllRegions();
    }

    private static class StubHttpConnector implements HttpConnector {
        boolean alert = false;

        @Override
        public InputStream connect(String path, String data, Redirect redirect) throws IOException {
            return new ByteArrayInputStream("[]".getBytes());
        }

        @Override
        public Map<String, List<String>> getHeaderFields(
                String path, String data, Redirect redirect) throws IOException {
            if (alert)
                throw new RuntimeException();
            return new HashMap<>();
        }
    }
}