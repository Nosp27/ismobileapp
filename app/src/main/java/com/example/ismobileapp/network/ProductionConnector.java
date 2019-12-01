package com.example.ismobileapp.network;

import android.util.JsonWriter;
import com.example.ismobileapp.model.*;
import com.example.ismobileapp.network.json.JSONModeller;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProductionConnector implements ApiConnector {
    private static final String SERVER = "";

    private static final String GET_ALL_REGIONS = "";
    private static final String GET_REGION = "";
    private static final String GET_ALL_CATEGORIES = "";
    private static final String GET_CATEGORY = "";

    public ProductionConnector() {

    }


    private JSONTokener readFromApi(String suffix) {
        HttpURLConnection connection;
        InputStream connInputStream;

        try {
            URL a = new URL(SERVER + suffix);
            connection = ((HttpURLConnection) a.openConnection());
            connInputStream = connection.getInputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
            // TODO: 01/12/2019 handle i/o exception
            return null;
        }

        try (InputStreamReader reader = new InputStreamReader(connInputStream)) {
            return tokenize(reader);
        } catch (IOException e) {
            try {
                connection.getResponseCode();
                InputStream es = connection.getErrorStream();

                // read the response body
                byte[] buf = new byte[30];
                while (es.read(buf) > 0) ;

                // close the errorstream
                es.close();
            } catch (IOException ee) {
            }
        }
        return null;
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
        if(tokener == null)
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

        if(tokener == null)
            return ret;
        try {
            while (tokener.nextValue() != JSONObject.NULL) {
                ret.add((JSONObject) tokener.nextValue());
            }
            return ret;
        } catch (JSONException e) {
            e.printStackTrace();
            // TODO: 01/12/2019 handle json exception
            return ret;
        }
    }

    @Override
    public List<Region> getAllRegions() {
        List<Region> ret = new ArrayList<>();
        for (JSONObject regionJSON : readJsonAsList(readFromApi(GET_ALL_REGIONS)))
            ret.add(JSONModeller.fromJSON(Region.class, regionJSON));
        return ret;
    }

    @Override
    public Region getRegion(int id) {
        return JSONModeller.fromJSON(Region.class, readJsonObject(readFromApi(GET_REGION+"/"+id)));
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> ret = new ArrayList<>();
        for (JSONObject regionJSON : readJsonAsList(readFromApi(GET_ALL_CATEGORIES)))
            ret.add(JSONModeller.fromJSON(Category.class, regionJSON));
        return ret;
    }

    @Override
    public List<Facility> getCriterizedFacilities(Criteries criteries) {
        JSONObject jsonRepr = JSONModeller.toJSON(criteries);
        return null;
    }
}
