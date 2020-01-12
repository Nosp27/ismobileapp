package com.example.ismobileapp.network;

import com.example.ismobileapp.resourceSupplier.ResourceSupplier;

import java.io.IOException;

public class Connectors {
    private static boolean debugMode = false;
    private static ResourceSupplier defaultConnector;

    public static ResourceSupplier getDefaultConnector() {
        if (defaultConnector == null)
            if (debugMode)
                defaultConnector = new StubConnector();
            else
                defaultConnector = new ProductionConnector();
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
