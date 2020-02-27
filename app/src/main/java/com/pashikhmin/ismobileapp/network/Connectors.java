package com.pashikhmin.ismobileapp.network;

import com.pashikhmin.ismobileapp.cache.CachingTask;
import com.pashikhmin.ismobileapp.resourceSupplier.ResourceSupplier;

import java.io.IOException;

public class Connectors {
    private static boolean debugMode = false;
    private static boolean cachingEnabled = true;

    private static ResourceSupplier defaultConnector;
    private static ResourceSupplier cachedConnector;

    private static String authenticityToken;

    public static String getAuthenticityToken() {
        return authenticityToken;
    }

    public static void setAuthenticityToken(String authenticityToken) {
        Connectors.authenticityToken = authenticityToken;
    }

    public static ResourceSupplier getDefaultCachedConnector() {
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
        if (cachingEnabled) {
            if(cachedConnector == null)
                cachedConnector = new CachingTask(defaultConnector).getResourceSupplier();
            return cachedConnector;
        }
        return defaultConnector;
    }

    public static void setDebugMode(boolean debugMode) {
        Connectors.debugMode = debugMode;
        defaultConnector = null;
        cachedConnector = null;
    }

    public static boolean pingServer() {
        try {
            ProductionConnector.pingServer();
            return true;
        } catch (IOException e) {
        }
        return false;
    }
}
