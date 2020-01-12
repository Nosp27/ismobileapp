package com.example.ismobileapp.network;

import android.util.Log;
import com.example.ismobileapp.cache.Cache;
import com.example.ismobileapp.cache.CachingTask;
import com.example.ismobileapp.resourceSupplier.ResourceSupplier;

import java.io.IOException;

public class Connectors {
    private static boolean debugMode = false;
    private static boolean cachingEnabled = true;
    private static ResourceSupplier defaultConnector;

    public static ResourceSupplier getDefaultConnector() {
        if (defaultConnector == null)
            if (debugMode)
                defaultConnector = new StubConnector();
            else {
                try {
                    defaultConnector = new ProductionConnector();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        if (cachingEnabled)
            defaultConnector = new CachingTask(defaultConnector).getResourceSupplier();
        return defaultConnector;
    }

    public static void setDebugMode(boolean debugMode) {
        Connectors.debugMode = debugMode;
        defaultConnector = null;
    }

    public static boolean pingServer() {
        try {
            ProductionConnector.getServerAddress();
            return true;
        } catch (IOException e) {
        }
        return false;
    }
}
