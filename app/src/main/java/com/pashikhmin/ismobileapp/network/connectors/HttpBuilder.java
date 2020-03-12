package com.pashikhmin.ismobileapp.network.connectors;

import com.pashikhmin.ismobileapp.cache.HttpCacheImpl;

public class HttpBuilder {
    private boolean cache = false;

    public HttpBuilder cache() {
        cache = true;
        return this;
    }

    public HttpConnector build() {
        HttpConnector straight = new HttpConnectorImpl();
        if (!cache)
            return straight;
        return new HttpCacheImpl(straight);
    }
}
