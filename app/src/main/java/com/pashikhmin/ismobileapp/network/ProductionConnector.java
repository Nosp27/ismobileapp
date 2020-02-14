package com.pashikhmin.ismobileapp.network;

import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import com.pashikhmin.ismobileapp.model.*;
import com.pashikhmin.ismobileapp.model.helpdesk.Actor;
import com.pashikhmin.ismobileapp.model.helpdesk.Issue;
import com.pashikhmin.ismobileapp.model.helpdesk.Message;
import com.pashikhmin.ismobileapp.network.exceptions.LoginRequiredException;
import com.pashikhmin.ismobileapp.network.json.JSONModeller;
import com.pashikhmin.ismobileapp.resourceSupplier.BinaryDataProvider;
import com.pashikhmin.ismobileapp.resourceSupplier.HelpDeskResourceSupplier;
import com.pashikhmin.ismobileapp.resourceSupplier.ResourceSupplier;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ProductionConnector implements ResourceSupplier, BinaryDataProvider, HelpDeskResourceSupplier {
    private static String namenode = "https://pastebin.com/raw/qnnpTbWk";
    private static String server;

    static final String GET_ALL_REGIONS = "/regions";
    static final String GET_ALL_CATEGORIES = "/categories";
    static final String GET_CRITERIZED_FACILITIES = "/facilities/";
    static final String READ_IMAGE_SUFFIX = "/image/";
    static final String GET_LIKED_FACILITIES = "/actor/favorites";
    static final String LIKE_FACILITY = "/actor/like";
    static final String FINGER = "/actor/me";

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

    public static String getServerAddress() {
        try {
            if (server == null) {
                server = loadServerIp();
            }
            return server;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load namenode: " + e);
        }
    }

    public static String pingServer() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(getServerAddress() + "/ping").openConnection();
        if (connection.getResponseCode() != 200)
            throw new IOException();
        return getServerAddress();
    }

    private static String loadServerIp() throws IOException {
        HttpURLConnection con = ((HttpURLConnection) new URL(namenode).openConnection());
        try (BufferedReader namenodeReader = new BufferedReader(
                new InputStreamReader(con.getInputStream())
        )) {
            return namenodeReader.readLine();
        }
    }

    static HttpURLConnection setupConnection(URL url) throws IOException {
        HttpURLConnection ret = ((HttpURLConnection) url.openConnection());
        ret.setConnectTimeout(TIMEOUT);
//        ret.setRequestProperty("Accept", "application/json");
        ret.setRequestProperty("Content-Type", "application/json");
        if (Connectors.getAuthenticityToken() != null) {
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
                url = new URL(pingServer() + suffix);
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
        addLikesForFacilities(ret);

        return ret;
    }

    private void addLikesForFacilities(List<Facility> ret) throws IOException {
        List<Facility> liked = getLikedFacilities();
        SparseArray<Facility> facilityMap = new SparseArray<>(ret.size());
        for (Facility f : ret)
            facilityMap.put(f.getId(), f);
        for (Facility f : liked) {
            facilityMap.get(f.getId()).setLiked(true);
        }
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

    @Override
    public boolean changeLike(Facility facility) throws IOException {
        JSONTokener tokener = readFromApi(String.format("%s/%s", LIKE_FACILITY, facility.getId()));
        try {
            return ((JSONObject) tokener.nextValue()).getBoolean("liked");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Facility> getLikedFacilities() throws IOException {
        List<Facility> ret = new ArrayList<>();
        for (JSONObject regionJSON : readJsonAsList(readFromApi(GET_LIKED_FACILITIES)))
            ret.add(JSONModeller.fromJSON(Facility.class, regionJSON));
        addImagesForEntities(ret);
        return ret;
    }

    @Override
    public Actor finger() throws IOException {
        return JSONModeller.fromJSON(Actor.class, readJsonObject(readFromApi(FINGER)));
    }

    @Override
    public List<Issue> getOpenedIssues() throws IOException {
        //TODO: implement
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Message> getIssueHistory(Issue issue) throws IOException {
        //TODO: implement
        throw new UnsupportedOperationException();
    }
}
