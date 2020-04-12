package com.pashikhmin.ismobileapp.network.connectors;

import com.pashikhmin.ismobileapp.cache.HttpCacheImpl;

import java.util.*;

public class HttpBuilder {
    private boolean cache = false;
    private Map<String, String> headers;

    public HttpBuilder cache() {
        cache = true;
        return this;
    }

    public HttpBuilder addHeader(String key, String value) {
        if (headers == null)
            headers = new HashMap<>();
        headers.put(key, value);
        return this;
    }

    public HttpConnector build() {
        HttpConnector straight = new HttpConnectorImpl();
        straight.addHeaders(headers);
        if (!cache)
            return straight;
        return new HttpCacheImpl(straight);
    }
}
