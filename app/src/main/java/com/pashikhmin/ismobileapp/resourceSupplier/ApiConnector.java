package com.pashikhmin.ismobileapp.resourceSupplier;

import android.graphics.drawable.Drawable;
import com.pashikhmin.ismobileapp.model.Category;
import com.pashikhmin.ismobileapp.model.Criteries;
import com.pashikhmin.ismobileapp.model.Facility;
import com.pashikhmin.ismobileapp.model.Region;
import com.pashikhmin.ismobileapp.model.helpdesk.Actor;
import com.pashikhmin.ismobileapp.model.helpdesk.Issue;
import com.pashikhmin.ismobileapp.model.helpdesk.Message;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ApiConnector {
//    String SERVER = "http://89.169.47.184:8080";
//    String SERVER = "http://10.0.2.2:8080";
    String SERVER = "http://23.111.202.14";
    String SECURE_PING = "/secure_ping";
    String GET_ALL_REGIONS = "/regions";
    String GET_ALL_CATEGORIES = "/categories";
    String GET_CRITERIZED_FACILITIES = "/facilities/";
    String READ_IMAGE_SUFFIX = "/image/";
    String GET_LIKED_FACILITIES = "/actor/favorites";
    String LIKE_FACILITY = "/actor/like";
    String FINGER = "/actor/me";

    String CREATE_ISSUE = "/help/issue";
    String LIST_ISSUES = "/help/issues";
    String WRITE_MESSAGE = "/help/message/send";
    String ISSUE_MESSAGES = "/help/issue/messages/";

    List<Region> getAllRegions() throws IOException;

    List<Category> getAllCategories() throws IOException;

    List<Facility> getCriterizedFacilities(Criteries criteries) throws IOException;

    boolean changeLike(Facility facility) throws IOException;

    List<Facility> getLikedFacilities() throws IOException;

    Drawable loadImage(Integer key) throws IOException;

    List<Issue> getOpenedIssues() throws IOException;

    List<Message> getIssueHistory(Issue issue) throws IOException;

    Issue createIssue(Issue issue) throws IOException;

    void sendMessage(Message message) throws IOException;

    Actor finger() throws IOException;
}
