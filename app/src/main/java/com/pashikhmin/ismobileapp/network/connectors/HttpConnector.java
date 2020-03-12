package com.pashikhmin.ismobileapp.network.connectors;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface HttpConnector {
    InputStream connect(String path, String data, Redirect redirect) throws IOException;
    Map<String, List<String>> getHeaderFields(String path, String data, Redirect redirect) throws IOException;
}
