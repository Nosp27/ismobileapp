package com.example.ismobileapp.network;

import java.io.IOException;

public class Connectors {
    private static boolean debugMode = false;
    private static ApiConnector defaultConnector;

    public static ApiConnector getDefaultConnector() {
        if (defaultConnector == null)
            if (debugMode)
                defaultConnector = new MockConnector();
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
