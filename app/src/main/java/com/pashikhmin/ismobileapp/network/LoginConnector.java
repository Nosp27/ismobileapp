package com.pashikhmin.ismobileapp.network;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginConnector {
    private HttpURLConnection setupConnection() throws IOException {
        String server = ProductionConnector.getServerAddress();
        int responseCode;
        HttpURLConnection connection = null;
        URL _url;

        do {
            if (connection == null)
                _url = new URL(server + "/secure_ping");
            else
                _url = new URL(connection.getHeaderField("Location"));
            connection = (HttpURLConnection) _url.openConnection();
            responseCode = connection.getResponseCode();
        } while (responseCode / 100 == 3);
        return connection;
    }

    private boolean isForm(HttpURLConnection connection) throws IOException {
        String contentType = connection.getHeaderField("Content-Type");
        return connection.getResponseCode() == 200 && (contentType != null && contentType.contains("text/html"));
    }

    public boolean validCredentials() throws IOException {
        HttpURLConnection connection = setupConnection();
        return !isForm(connection);
    }

    public Uri validateCredentialsUrl() throws IOException {
        HttpURLConnection connection = setupConnection();
        if (!isForm(connection))
            return null;

        return Uri.parse(ProductionConnector.getServerAddress() + "/secure_login");
    }
}
