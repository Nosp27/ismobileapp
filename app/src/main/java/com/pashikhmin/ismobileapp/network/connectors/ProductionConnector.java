package com.pashikhmin.ismobileapp.network.connectors;

import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import com.pashikhmin.ismobileapp.cache.CacheWarmer;
import com.pashikhmin.ismobileapp.model.*;
import com.pashikhmin.ismobileapp.model.helpdesk.Actor;
import com.pashikhmin.ismobileapp.model.helpdesk.Issue;
import com.pashikhmin.ismobileapp.model.helpdesk.Message;
import com.pashikhmin.ismobileapp.network.json.JSONModeller;
import com.pashikhmin.ismobileapp.network.json.JSONParser;
import com.pashikhmin.ismobileapp.resourceSupplier.CredentialsResourceSupplier;
import com.pashikhmin.ismobileapp.resourceSupplier.HelpDeskResourceSupplier;
import com.pashikhmin.ismobileapp.resourceSupplier.ApiConnector;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductionConnector implements
        ApiConnector,
        CredentialsResourceSupplier {
    String server = ApiConnector.SERVER;

    HttpConnector httpConnector;
    JSONParser jsonParser;
    CredentialsResourceSupplier credentialsResourceSupplier;
    CacheWarmer cacheWarmer;

    RESTConnector restConnector;

    ProductionConnector(
            ConnectorBuilder builder
    ) {
        this.httpConnector = builder.httpConnector;
        this.restConnector = builder.restConnector;
        this.jsonParser = builder.jsonParser;
        this.credentialsResourceSupplier = builder.credentialsResourceSupplier;
    }

    String pingServer() throws IOException {
        String pingPath = server + "/ping";
        restConnector.get(pingPath);
        return server;
    }

    @Override
    public List<Region> getAllRegions() throws IOException {
        return jsonParser.readList(Region.class, restConnector.get(GET_ALL_REGIONS));
    }

    @Override
    public List<Category> getAllCategories() throws IOException {
        return jsonParser.readList(Category.class, restConnector.get(GET_ALL_CATEGORIES));
    }

    @Override
    public List<Facility> getCriterizedFacilities(Criteries criteries) throws IOException {
        List<Facility> ret = new ArrayList<>();
        JSONObject criteriesJson = JSONModeller.toJSON(criteries);
        if (criteriesJson == null)
            return ret;
        String postData = criteries.toString();

        ret.addAll(jsonParser.readList(
                Facility.class,
                restConnector.post(GET_CRITERIZED_FACILITIES, postData)
        ));
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
            entity.setImage(loadImage(entity.getImageId()));
        }
    }

    @Override
    public Drawable loadImage(Integer key) throws IOException {
        if (key == null)
            return null;
        return Drawable.createFromStream(
                httpConnector.connect(
                        READ_IMAGE_SUFFIX + key, null, Redirect.FOLLOW
                ),
                null
        );
    }

    @Override
    public boolean changeLike(Facility facility) throws IOException {
        JSONTokener tokener = restConnector.get(String.format("%s/%s", LIKE_FACILITY, facility.getId()));
        try {
            return ((JSONObject) tokener.nextValue()).getBoolean("liked");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Facility> getLikedFacilities() throws IOException {
        List<Facility> ret = new ArrayList<>();
        jsonParser.readList(Facility.class, restConnector.get(GET_LIKED_FACILITIES));
        addImagesForEntities(ret);
        return ret;
    }

    @Override
    public Actor finger() throws IOException {
        return jsonParser.readObject(Actor.class, restConnector.get(FINGER));
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

    @Override
    public String getCookie(String username, String password) throws IOException {
        return credentialsResourceSupplier.getCookie(username, password);
    }
}
