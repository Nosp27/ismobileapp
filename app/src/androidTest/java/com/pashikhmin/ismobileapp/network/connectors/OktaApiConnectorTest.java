package com.pashikhmin.ismobileapp.network.connectors;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.pashikhmin.ismobileapp.resourceSupplier.ApiConnector;
import com.pashikhmin.ismobileapp.resourceSupplier.CredentialsResourceSupplier;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class OktaApiConnectorTest {
    @Test
    public void fetchCookie() throws IOException {
        HttpConnector httpConnector = Connectors.createHttp();
        RESTConnector oktaRest = new RESTBuilder().server(Connectors.OKTA_URL).wrap(httpConnector).build();
        CredentialsResourceSupplier crs = new OktaResourceSupplier(httpConnector, oktaRest);
        String cookie = crs.getCookie("dixid96666@winemail.net", "Password123");
        Assert.assertNotNull(cookie);

        boolean thrown = false;
        try {
            crs = new OktaResourceSupplier(Connectors.createHttp(), Connectors.createRest());
            cookie = crs.getCookie("dixid96666@winemail.net", "SomeWrongPassword");
            Assert.assertNotNull(cookie);
        } catch (IOException e) {
            thrown = true;
        }

        Assert.assertTrue(thrown);
    }
}