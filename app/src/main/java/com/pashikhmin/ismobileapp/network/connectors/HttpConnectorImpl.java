package com.pashikhmin.ismobileapp.network.connectors;

import com.pashikhmin.ismobileapp.network.exceptions.LoginRequiredException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpConnectorImpl implements HttpConnector {
    private static final int TIMEOUT = 1000;
    private Map<String, String> headers;

    HttpConnectorImpl() {
        headers = new HashMap<>();
        addHeader("Content-Type", "application/json");
        if (CookieManager.getDefault() == null)
            CookieManager.setDefault(new CookieManager());
    }

    @Override
    public InputStream connect(String path, String content, Redirect redirect) throws IOException {
        return connect0(path, content, redirect).getInputStream();
    }

    @Override
    public Map<String, List<String>> getHeaderFields(String path, String content, Redirect redirect) throws IOException {
        return connect0(path, content, redirect).getHeaderFields();
    }


    void addHeader(String name, String value) {
        headers.put(name, value);
    }

    @Override
    public void addHeaders(Map<String, String> headers) {
        if (headers != null)
            this.headers.putAll(headers);
    }

    private HttpURLConnection connect0(String path, String content, Redirect redirect) throws IOException {
        HttpURLConnection connection = null;
        int responseCode;
        URL url;

        // follow redirects
        do {
            if (connection != null) {
                url = new URL(connection.getHeaderField("Location"));
            } else
                url = new URL(path);

            connection = setupConnection(url);
            if (content != null) {
                connection.setRequestMethod("POST");
                connection.getOutputStream().write(content.getBytes());
            }
            responseCode = connection.getResponseCode();
        } while (responseCode / 100 == 3 && redirect.equals(Redirect.FOLLOW));
        if (redirect == Redirect.RETURN)
            return connection;
        if (connection.getHeaderField("Content-Type").contains("text/html") || responseCode == 403) {
            throw new LoginRequiredException();
        }
        if (connection.getResponseCode() != 200) {
            String error = new BufferedReader(new InputStreamReader(connection.getErrorStream())).readLine();
            if(error != null)
                throw new IOException(error);
            throw new IOException("Got response code " + connection.getResponseCode());
        }

        return connection;
    }

    private HttpURLConnection setupConnection(URL url) throws IOException {
        HttpURLConnection ret = ((HttpURLConnection) url.openConnection());
        ret.setConnectTimeout(TIMEOUT);
        for (Map.Entry<String, String> entry : headers.entrySet())
            ret.setRequestProperty(entry.getKey(), entry.getValue());
        if (Connectors.getAuthenticityToken() != null) {
            ret.setRequestProperty("Cookie", Connectors.getAuthenticityToken());
        }
        return ret;
    }
}
