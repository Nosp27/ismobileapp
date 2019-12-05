package com.example.ismobileapp.network;

import com.example.ismobileapp.model.Category;
import com.example.ismobileapp.model.Criteries;
import com.example.ismobileapp.model.Facility;
import com.example.ismobileapp.model.Region;
import com.example.ismobileapp.network.json.JSONModeller;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProductionConnector implements ApiConnector {
    static final String SERVER = "http://192.168.1.56:8080";

    static final String GET_ALL_REGIONS = "/regions";
    static final String GET_REGION = "/region";
    static final String GET_ALL_CATEGORIES = "/categories";
    static final String GET_CRITERIZED_FACILITIES = "/facilities/";

    public ProductionConnector() {
    }

    static HttpURLConnection setupConnection(URL url) throws IOException {
        HttpURLConnection ret = ((HttpURLConnection) url.openConnection());
        ret.setRequestProperty("Content-Type", "application/json");
        return ret;
    }

    private JSONTokener readFromApi(String suffix) {
        return readFromApi(suffix, null);
    }

    private JSONTokener readFromApi(String suffix, String content) {
        HttpURLConnection connection;
        InputStream connInputStream;

        try {
            URL url = new URL(SERVER + suffix);
            connection = setupConnection(url);
            if (content != null) {
                connection.setRequestMethod("POST");
                connection.getOutputStream().write(content.getBytes());
            }
            if(connection.getResponseCode() != 200)
                throw new IOException("Unsuccessful response");
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
        return JSONModeller.fromJSON(Region.class, readJsonObject(readFromApi(GET_REGION + "/" + id)));
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
        List<Facility> ret = new ArrayList<>();
        JSONObject criteriesJson = JSONModeller.toJSON(criteries);
        if (criteriesJson == null)
            return ret;

        for (JSONObject regionJSON : readJsonAsList(readFromApi(GET_CRITERIZED_FACILITIES, criteriesJson.toString())))
            ret.add(JSONModeller.fromJSON(Facility.class, regionJSON));
        return ret;
    }
}
