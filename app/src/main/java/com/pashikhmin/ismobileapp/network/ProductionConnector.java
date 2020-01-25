package com.pashikhmin.ismobileapp.network;

import android.app.AuthenticationRequiredException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import com.pashikhmin.ismobileapp.model.*;
import com.pashikhmin.ismobileapp.network.exceptions.LoginRequiredException;
import com.pashikhmin.ismobileapp.network.json.JSONModeller;
import com.pashikhmin.ismobileapp.resourceSupplier.BinaryDataProvider;
import com.pashikhmin.ismobileapp.resourceSupplier.ResourceSupplier;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.*;
import java.util.*;

public class ProductionConnector implements ResourceSupplier, BinaryDataProvider {
    static final List<String> SERVER_ENDPOINTS = Arrays.asList(
            "http://192.168.43.56:8080",
            "http://192.168.1.56:8080"
    );
    private static String server;

    static final String GET_ALL_REGIONS = "/regions";
    static final String GET_REGION = "/region";
    static final String GET_ALL_CATEGORIES = "/categories";
    static final String GET_CRITERIZED_FACILITIES = "/facilities/";
    static final String READ_IMAGE_SUFFIX = "/image/";

    private static final int TIMEOUT = 1000;

    private BinaryDataProvider binaryDataProvider;

    public ProductionConnector() throws IOException {
        setBinaryDataProvider(this);
    }

    public BinaryDataProvider getBinaryDataProvider() {
        return binaryDataProvider;
    }

    public void setBinaryDataProvider(BinaryDataProvider binaryDataProvider) {
        this.binaryDataProvider = binaryDataProvider;
    }

    static String getServerAddress() throws IOException {
        if (server == null)
            server = figureOutIpAddress();
        return server;
    }

    private static String figureOutIpAddress() throws IOException {
        Iterator<String> endpointIterator = SERVER_ENDPOINTS.iterator();
        String possibleAddress = endpointIterator.next();
        for (; ; ) {
            try {
                HttpURLConnection connection = setupConnection(new URL(possibleAddress + "/ping"));
                if (connection.getResponseCode() == 200) {
                    connection.disconnect();
                    return possibleAddress;
                } else
                    throw new IOException("Unexpected response code: " + connection.getResponseCode());
            } catch (IOException e) {
                if (!endpointIterator.hasNext())
                    throw e;
                possibleAddress = endpointIterator.next();
            }
        }
    }

    static HttpURLConnection setupConnection(URL url) throws IOException {
        HttpURLConnection ret = ((HttpURLConnection) url.openConnection());
        ret.setConnectTimeout(TIMEOUT);
        ret.setRequestProperty("Accept", "application/json");
        ret.setRequestProperty("Content-Type", "application/json");
        if(Connectors.getAuthenticityToken() != null) {
            ret.setRequestProperty("Cookie", Connectors.getAuthenticityToken());
        }
        return ret;
    }

    private JSONTokener readFromApi(String suffix) throws IOException {
        return readFromApi(suffix, null);
    }

    private JSONTokener readFromApi(String suffix, String content) throws IOException {
        InputStream connInputStream = connectToApi(suffix, content);
        try (InputStreamReader reader = new InputStreamReader(connInputStream)) {
            return tokenize(reader);
        } catch (LoginRequiredException e) {
            throw e;
        } catch (IOException e) {
        }
        return null;
    }

    InputStream connectToApi(String suffix, String content) throws IOException {
        HttpURLConnection connection = null;
        int responseCode;
        URL url;
        do {
            if (connection != null) {
                url = new URL(connection.getHeaderField("Location"));
            } else
                url = new URL(getServerAddress() + suffix);
            connection = setupConnection(url);
            if (content != null) {
                connection.setRequestMethod("POST");
                connection.getOutputStream().write(content.getBytes());
            }
            responseCode = connection.getResponseCode();
        } while (responseCode / 100 == 3);
        if (connection.getHeaderField("Content-Type").contains("text/html") || responseCode == 403) {
            throw new LoginRequiredException();
        }
        if (responseCode != 200)
            throw new ConnectException("Unsuccessful response: " + responseCode);
        return connection.getInputStream();
    }


    private JSONTokener tokenize(Reader reader) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONTokener(sb.toString());
    }

    private JSONObject readJsonObject(JSONTokener tokener) {
        if (tokener == null)
            return null;

        try {
            return (JSONObject) tokener.nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
            // TODO: 01/12/2019 handle json exception
            return null;
        }
    }

    private List<JSONObject> readJsonAsList(JSONTokener tokener) {
        List<JSONObject> ret = new ArrayList<>();

        if (tokener == null)
            return ret;
        try {
            JSONArray nextTokens = ((JSONArray) tokener.nextValue());
            for (int i = 0; i < nextTokens.length(); i++)
                ret.add((JSONObject) nextTokens.get(i));
            return ret;
        } catch (JSONException e) {
            e.printStackTrace();
            // TODO: 01/12/2019 handle json exception
            return ret;
        }
    }

    @Override
    public List<Region> getAllRegions() throws IOException {
        List<Region> ret = new ArrayList<>();
        for (JSONObject regionJSON : readJsonAsList(readFromApi(GET_ALL_REGIONS)))
            ret.add(JSONModeller.fromJSON(Region.class, regionJSON));
        addImagesForEntities(ret);

        return ret;
    }

    @Override
    public List<Category> getAllCategories() throws IOException {
        List<Category> ret = new ArrayList<>();
        for (JSONObject regionJSON : readJsonAsList(readFromApi(GET_ALL_CATEGORIES)))
            ret.add(JSONModeller.fromJSON(Category.class, regionJSON));
        addImagesForEntities(ret);

        return ret;
    }

    @Override
    public List<Facility> getCriterizedFacilities(Criteries criteries) throws IOException {
        List<Facility> ret = new ArrayList<>();
        JSONObject criteriesJson = JSONModeller.toJSON(criteries);
        if (criteriesJson == null)
            return ret;

        for (JSONObject regionJSON : readJsonAsList(readFromApi(GET_CRITERIZED_FACILITIES, criteriesJson.toString())))
            ret.add(JSONModeller.fromJSON(Facility.class, regionJSON));
        addImagesForEntities(ret);

        return ret;
    }

    private void addImagesForEntities(List<? extends Entity> list) throws IOException {
        for (Entity entity : list) {
            entity.setImage(getBinaryDataProvider().loadImage(entity.getImageId()));
        }
    }

    @Override
    public Drawable loadImage(Integer key) throws IOException {
        if (key == null)
            return null;
        return Drawable.createFromStream(connectToApi(READ_IMAGE_SUFFIX + key, null), null);
    }
}
