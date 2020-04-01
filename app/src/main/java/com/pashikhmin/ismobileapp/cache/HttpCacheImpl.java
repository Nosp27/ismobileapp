package com.pashikhmin.ismobileapp.cache;

import com.pashikhmin.ismobileapp.network.connectors.HttpConnector;
import com.pashikhmin.ismobileapp.network.connectors.Redirect;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpCacheImpl implements HttpConnector {
    HttpConnector underlying;
    Map<String, Byte[]> cacheMapGET;
    Map<String, Byte[]> cacheMapPOST;

    public HttpCacheImpl(HttpConnector underlying) {
        cacheMapGET = new HashMap<>();
        cacheMapPOST = new HashMap<>();
        this.underlying = underlying;
    }

    @Override
    public InputStream connect(String path, String data, Redirect redirect) throws IOException {
        return underlying.connect(path, data, redirect);
    }

    @Override
    public Map<String, List<String>> getHeaderFields(String path, String data, Redirect redirect) throws IOException {
        return underlying.getHeaderFields(path, data, redirect);
    }
}
