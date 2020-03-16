package com.pashikhmin.ismobileapp.network.connectors;

import com.pashikhmin.ismobileapp.network.exceptions.AuthenticationFailedException;
import com.pashikhmin.ismobileapp.resourceSupplier.ApiConnector;
import com.pashikhmin.ismobileapp.resourceSupplier.CredentialsResourceSupplier;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class OktaResourceSupplier implements CredentialsResourceSupplier {
    private static Random random = new Random();
    private String server = ApiConnector.SERVER;

    private HttpConnector httpConnector;
    private RESTConnector oktaRestConnector;

    private static final String SESSION_TOKEN_URL = "/api/v1/authn";
    private static final String AUTHORIZE_URL = "/oauth2/v1/authorize";

    OktaResourceSupplier(HttpConnector connector, RESTConnector restConnector) {
        httpConnector = connector;
        oktaRestConnector = restConnector;
    }

    @Override
    public String getCookie(String username, String password) throws IOException {
        String sessionToken = getSessionToken(username, password);
        String cookie = introspectToken(sessionToken);
        validateCookie(cookie);
        return cookie;
    }

    private String getSessionToken(String username, String password) throws IOException {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("username", username);
        requestData.put("password", password);
        requestData.put("warnBeforePasswordExpired", true);
        requestData.put("multiOptionalFactorEnroll", true);
        JSONObject requestDataJSON = new JSONObject(requestData);
        JSONTokener authRespTokener = null;

        try {
            authRespTokener = oktaRestConnector.post(
                    SESSION_TOKEN_URL, requestDataJSON.toString()
            );
        } catch (IOException e) {
            String message = e.getMessage();
            if (message!=null && message.contains("401"))
                throw  new AuthenticationFailedException();
            throw e;
        }

        JSONObject authRespJson;
        String sessionToken;
        try {
            authRespJson = new JSONObject(authRespTokener);
            if (authRespJson.has("errorSummary"))
                throw new AuthenticationFailedException();

            sessionToken = authRespJson.getString("sessionToken");
        } catch (JSONException e) {
            throw new IOException("Wrong content format!", e);
        }
        return sessionToken;
    }

    private String introspectToken(String token) throws IOException {
        Map<String, String> queryargs = new HashMap<>();
        queryargs.put("client_id", "0oa11tnym3wzviaV34x6");
        queryargs.put("response_type", "id_token");
        queryargs.put("scope", "openid");
        queryargs.put("nonce", "some nonce");
        queryargs.put("prompt", "none");
        queryargs.put("redirect_uri", server + "/actor/login_callback");
        queryargs.put("state", Integer.toString(Integer.hashCode(random.nextInt())).substring(0, 5)); // random state
        queryargs.put("sessionToken", token);

        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, String> param : queryargs.entrySet()) {
            if (queryString.length() != 0) queryString.append('&');
            queryString.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            queryString.append('=');
            queryString.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        Map<String, List<String>> headerFields = httpConnector.getHeaderFields(
                Connectors.OKTA_URL + AUTHORIZE_URL + "/?" + queryString, null, Redirect.RETURN
        );
        String cookie = null;
        for (String c : headerFields.get("Set-Cookie"))
            if (c.contains("sid")) {
                cookie = c;
                break;
            }
        if (cookie == null)
            throw new IOException("No Cookie found");
        return cookie.split(";")[0];
    }

    private void validateCookie(String cookie) throws IOException {
        try {
            Connectors.setAuthenticityToken(cookie);
            InputStream is = httpConnector.connect(server + "/secure_ping", null, Redirect.FOLLOW);
            if (!new BufferedReader(new InputStreamReader(is)).readLine().equals("Login successful!"))
                throw new IOException("Login not successful!");
        } catch (IOException e) {
            throw new IOException("Cookie has not been validated!", e);
        }
    }
}
