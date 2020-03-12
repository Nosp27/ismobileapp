package com.pashikhmin.ismobileapp.network.connectors;

import org.json.JSONTokener;

import java.io.*;


/**
 * URL String -> JSONTokener
 */
public class RESTConnectorImpl implements RESTConnector {
    private final String servername;
    HttpConnector httpConnector;

    RESTConnectorImpl(String servername, HttpConnector httpConnector) {
        this.servername = servername;
        this.httpConnector = httpConnector;
    }

    @Override
    public JSONTokener get(String path) throws IOException {
        return tokenize(new InputStreamReader(
                httpConnector.connect(servername + path, null, Redirect.FOLLOW)
        ));
    }

    @Override
    public JSONTokener post(String path, String data) throws IOException {
        return tokenize(new InputStreamReader(
                httpConnector.connect(servername + path, data, Redirect.FOLLOW)
        ));
    }

    private JSONTokener tokenize(Reader reader) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONTokener(sb.toString());
    }
}
