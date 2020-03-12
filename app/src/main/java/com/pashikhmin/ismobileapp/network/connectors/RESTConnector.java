package com.pashikhmin.ismobileapp.network.connectors;

import org.json.JSONTokener;

import java.io.IOException;

public interface RESTConnector {
    JSONTokener get(String path) throws IOException;
    JSONTokener post(String path, String data) throws IOException;
}
