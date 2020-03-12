package com.pashikhmin.ismobileapp.network.connectors;

import com.pashikhmin.ismobileapp.network.json.JSONModeller;
import com.pashikhmin.ismobileapp.resourceSupplier.ApiConnector;

public class Connectors {
    //    static final String API_ADDRESS = "http://89.169.47.184:8080";
    static final String OKTA_URL = "https://dev-478832.okta.com";
    private static boolean debugMode = false;

    private static ApiConnector defaultConnector;

    private static String authenticityToken;

    public static String getAuthenticityToken() {
        return authenticityToken;
    }

    public static void setAuthenticityToken(String authenticityToken) {
        Connectors.authenticityToken = authenticityToken;
    }

    public static ApiConnector api() {
        if (defaultConnector == null)
            defaultConnector = debugMode ? createStub() : assembleProductionApi();
        return defaultConnector;
    }

    public static void setCustomConnector(ApiConnector customConnector) {
        defaultConnector = customConnector;
    }

    public static ApiConnector createStub() {
        return new StubConnector();
    }

    public static HttpConnector createHttp() {
        return new HttpBuilder().build();
    }

    public static HttpConnector createCachedHttp() {
        return new HttpBuilder()
                .cache()
                .build();
    }

    public static RESTConnector createRest() {
        return new RESTBuilder()
                .server(ApiConnector.SERVER)
                .wrap(createHttp())
                .build();
    }

    public static RESTConnector createCachedRest() {
        return new RESTBuilder()
                .server(ApiConnector.SERVER)
                .wrap(createHttp())
                .cache()
                .build();
    }

    private static ApiConnector assembleProductionApi() {
        HttpConnector httpConnector = createHttp();
        HttpConnector cachedHttpConnector = createCachedHttp();
        RESTConnector cachedRestConnector = new RESTBuilder()
                .wrap(httpConnector)
                .server(ApiConnector.SERVER)
                .cache()
                .build();
        return new ConnectorBuilder()
                .imageLoader(cachedHttpConnector)
                .credential(
                        new OktaResourceSupplier(httpConnector, new RESTBuilder()
                                .wrap(httpConnector)
                                .server(OKTA_URL)
                                .build()
                        ))
                .rest(cachedRestConnector)
                .parser(new JSONModeller())
                .build();
    }
}
