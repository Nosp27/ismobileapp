package com.example.ismobileapp.network;

import android.graphics.drawable.Drawable;
import com.example.ismobileapp.model.Category;
import com.example.ismobileapp.model.Criteries;
import com.example.ismobileapp.model.Facility;
import com.example.ismobileapp.model.Region;
import com.example.ismobileapp.network.json.JSONModeller;
import com.example.ismobileapp.resourceSupplier.ResourceSupplier;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ProductionConnector implements ResourceSupplier {
    static final List<String> SERVER_ENDPOINTS = Arrays.asList(
            "http://192.168.1.56:8080",
            "http://192.168.88.233:8080",
            "http://192.168.4.113:8080"
    );
    private static String server;

    static final String GET_ALL_REGIONS = "/regions";
    static final String GET_REGION = "/region";
    static final String GET_ALL_CATEGORIES = "/categories";
    static final String GET_CRITERIZED_FACILITIES = "/facilities/";
    static final String READ_IMAGE_SUFFIX = "/image/";

    private static final int TIMEOUT = 1000;

    public ProductionConnector() throws IOException {
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
                HttpURLConnection connection = setupConnection(new URL(possibleAddress + "/regions"));
                if (connection.getResponseCode() == 200) {
                    connection.disconnect();
                    return possibleAddress;
                } else
                    throw new IOException("Unexpected response code");
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
        ret.setRequestProperty("Content-Type", "application/json");
        return ret;
    }

    private JSONTokener readFromApi(String suffix) throws IOException {
        return readFromApi(suffix, null);
    }

    private JSONTokener readFromApi(String suffix, String content) throws IOException {
        InputStream connInputStream = connectToApi(suffix, content);
        try (InputStreamReader reader = new InputStreamReader(connInputStream)) {
            return tokenize(reader);
        } catch (IOException e) {
        }
        return null;
    }

    InputStream connectToApi(String suffix, String content) throws IOException {
        HttpURLConnection connection;
        URL url = new URL(getServerAddress() + suffix);
        connection = setupConnection(url);
        if (content != null) {
            connection.setRequestMethod("POST");
            connection.getOutputStream().write(content.getBytes());
        }
        int responseCode = connection.getResponseCode();
        if (responseCode != 200)
            throw new IOException("Unsuccessful response: " + responseCode);
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
        return ret;
    }

    @Override
    public List<Category> getAllCategories() throws IOException {
        List<Category> ret = new ArrayList<>();
        for (JSONObject regionJSON : readJsonAsList(readFromApi(GET_ALL_CATEGORIES)))
            ret.add(JSONModeller.fromJSON(Category.class, regionJSON));
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
        return ret;
    }

    @Override
    public Drawable loadImage(Integer key) throws IOException {
        return Drawable.createFromStream(connectToApi(READ_IMAGE_SUFFIX + key, null), null);
    }
}
