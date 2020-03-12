package com.pashikhmin.ismobileapp.network.connectors;

import com.pashikhmin.ismobileapp.cache.CacheWarmer;
import com.pashikhmin.ismobileapp.network.json.JSONParser;
import com.pashikhmin.ismobileapp.resourceSupplier.ApiConnector;
import com.pashikhmin.ismobileapp.resourceSupplier.CredentialsResourceSupplier;

public class ConnectorBuilder {
    HttpConnector httpConnector;
    JSONParser jsonParser;
    CredentialsResourceSupplier credentialsResourceSupplier;
    RESTConnector restConnector;
    CacheWarmer warmer;

    public ConnectorBuilder imageLoader(HttpConnector connector) {
        this.httpConnector = connector;
        return this;
    }

    public ConnectorBuilder rest(RESTConnector connector) {
        this.restConnector = connector;
        return this;
    }

    public ConnectorBuilder parser(JSONParser parser) {
        this.jsonParser = parser;
        return this;
    }

    public ConnectorBuilder credential(CredentialsResourceSupplier rs) {
        credentialsResourceSupplier = rs;
        return this;
    }

    public ConnectorBuilder warmer(CacheWarmer warmer) {
        this.warmer = warmer;
        return this;
    }

    public ApiConnector build() {
        return new ProductionConnector(this);
    }
}
