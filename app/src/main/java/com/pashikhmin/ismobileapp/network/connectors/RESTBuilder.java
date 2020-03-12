package com.pashikhmin.ismobileapp.network.connectors;

import com.pashikhmin.ismobileapp.cache.RESTCacheImpl;

public class RESTBuilder {
    private boolean cached = false;
    private HttpConnector underlying;
    private String server;

    public RESTBuilder wrap(HttpConnector wrapped) {
        this.underlying = wrapped;
        return this;
    }

    public RESTBuilder cache() {
        cached = true;
        return this;
    }

    public RESTBuilder server(String server) {
        this.server = server;
        return this;
    }

    public RESTConnector build() {
        RESTConnector straight = new RESTConnectorImpl(server, underlying);
        if(!cached)
            return straight;
        return new RESTCacheImpl(straight);
    }
}
