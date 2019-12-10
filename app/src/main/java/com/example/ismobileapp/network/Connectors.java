package com.example.ismobileapp.network;

public class Connectors {
    private static boolean debugMode = true;
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
    }
}
