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
    HttpConnector httpConnector;
    JSONParser jsonParser;
    CredentialsResourceSupplier credentialsResourceSupplier;
    CacheWarmer cacheWarmer; // TODO: warm caches

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
        String pingPath = ApiConnector.SERVER + "/ping";
        restConnector.get(pingPath);
        return ApiConnector.SERVER;
    }

    @Override
    public List<Region> getAllRegions() throws IOException {
        List<Region> regions = jsonParser.readList(Region.class, restConnector.get(GET_ALL_REGIONS));
        addImagesForEntities(regions);
        return regions;
    }

    @Override
    public List<Category> getAllCategories() throws IOException {
        List<Category> categories = jsonParser.readList(Category.class, restConnector.get(GET_ALL_CATEGORIES));
        addImagesForEntities(categories);
        return categories;
    }

    @Override
    public List<Facility> getCriterizedFacilities(Criteries criteries) throws IOException {
        List<Facility> ret = new ArrayList<>();
        JSONObject criteriesJson = JSONModeller.toJSON(criteries);
        if (criteriesJson == null)
            throw new IllegalArgumentException("Criterias must not be null");
        String postData = criteriesJson.toString();

        ret.addAll(jsonParser.readList(
                Facility.class,
                restConnector.post(GET_CRITERIZED_FACILITIES, postData)
        ));
        addImagesForEntities(ret);

        if (Connectors.userAuthorized())
            addLikesForFacilities(ret);
        else for (Facility facility : ret)
            facility.setLiked(null);

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
                        ApiConnector.SERVER + READ_IMAGE_SUFFIX + key, null, Redirect.FOLLOW
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
        List<Facility> ret = jsonParser.readList(Facility.class, restConnector.get(GET_LIKED_FACILITIES));
        // TODO: BAD
        addImagesForEntities(ret);
        return ret;
    }

    @Override
    public Actor finger() throws IOException {
        return jsonParser.readObject(Actor.class, restConnector.get(FINGER));
    }

    @Override
    public List<Issue> getOpenedIssues() throws IOException {
        return jsonParser.readList(Issue.class, restConnector.get(LIST_ISSUES));
    }

    @Override
    public List<Message> getIssueHistory(Issue issue) throws IOException {
        return jsonParser.readList(Message.class, restConnector.get(ISSUE_MESSAGES + issue.getId()));
    }

    @Override
    public List<Message> getNewMessages(int issue_id, long timestamp) throws IOException {
        return jsonParser.readList(Message.class, restConnector.get(
                String.format(ISSUE_NEW_MESSAGES, issue_id, timestamp)
        ));
    }

    @Override
    public void sendMessage(Message toSend) throws IOException {
        restConnector.post(ApiConnector.WRITE_MESSAGE, JSONModeller.toJSON(toSend).toString());
    }

    @Override
    public Issue createIssue(Issue issue) throws IOException {
        return jsonParser.readObject(Issue.class, restConnector.post(
                ApiConnector.CREATE_ISSUE, JSONModeller.toJSON(issue).toString())
        );
    }

    @Override
    public String getCookie(String username, String password) throws IOException {
        return credentialsResourceSupplier.getCookie(username, password);
    }
}
