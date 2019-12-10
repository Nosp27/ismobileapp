package com.example.ismobileapp.network;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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
            HttpURLConnection conn =
                    ProductionConnector.setupConnection(
                            new URL(ProductionConnector.SERVER + ProductionConnector.GET_ALL_REGIONS)
                    );
            if (conn.getResponseCode() == 200) {
                return true;
            }
        } catch (IOException e) {
        }
        return false;
    }
}
