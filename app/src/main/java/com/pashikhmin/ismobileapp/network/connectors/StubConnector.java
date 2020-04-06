package com.pashikhmin.ismobileapp.network.connectors;

import android.graphics.drawable.Drawable;
import com.pashikhmin.ismobileapp.model.Category;
import com.pashikhmin.ismobileapp.model.Criteries;
import com.pashikhmin.ismobileapp.model.Facility;
import com.pashikhmin.ismobileapp.model.Region;
import com.pashikhmin.ismobileapp.model.helpdesk.Actor;
import com.pashikhmin.ismobileapp.model.helpdesk.Issue;
import com.pashikhmin.ismobileapp.model.helpdesk.Message;
import com.pashikhmin.ismobileapp.resourceSupplier.CredentialsResourceSupplier;
import com.pashikhmin.ismobileapp.resourceSupplier.HelpDeskResourceSupplier;
import com.pashikhmin.ismobileapp.resourceSupplier.ApiConnector;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

public class StubConnector implements ApiConnector, CredentialsResourceSupplier {
    private List<Facility> recordedLikeList = new ArrayList<>();

    // Help Desk Data
    private static final Actor investor = new Actor("Some investor");
    private static final Actor manager = new Actor("Some manager");
    private static final Issue issue1 = new Issue("Want to invest in Rosneft");
    private static final Issue issue2 = new Issue("Cannot load my favorite facilities");
    private static final List<Message> messagesForIssue1 = Arrays.asList(
            new Message("Hello! This is a test message from test manager", false),
            new Message("Hello, this is a test response from investor", true)
    );
    private static final List<Message> messagesForIssue2 = Arrays.asList(
            new Message("Test message for issue2. Sorry for those not loaded facilies", false),
            new Message("Ok.", true)
    );

    protected StubConnector() {
    }

    @Override
    public List<Region> getAllRegions() {
        return Arrays.asList(
                new Region(0, "Moscow"),
                new Region(1, "Balashikha"),
                new Region(2, "SPB")
        );
    }


    @Override
    public List<Category> getAllCategories() {
        return Arrays.asList(
                new Category(0, "Property"),
                new Category(1, "IT Industry"),
                new Category(2, "Factories")
        );
    }

    @Override
    public List<Facility> getCriterizedFacilities(Criteries criteries) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(
                new Facility("Hse", new Double[]{55.23, 34.33}),
                new Facility("SAS", new Double[]{15.11, 24.32})
        );
    }

    @Override
    public boolean changeLike(Facility facility) throws IOException {
        boolean becameLiked;
        if (recordedLikeList.contains(facility)) {
            recordedLikeList.remove(facility);
            becameLiked = false;
        } else {
            recordedLikeList.add(facility);
            becameLiked = true;
        }
        return becameLiked;
    }

    @Override
    public List<Facility> getLikedFacilities() throws IOException {
        return recordedLikeList;
    }

    @Override
    public Drawable loadImage(Integer key) {
        byte[] bts = new byte[]{
                -119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0,
                13, 73, 72, 68, 82, 0, 0, 0, 1, 0,
                0, 0, 1, 1, 3, 0, 0, 0, 37, -37,
                86, -54, 0, 0, 0, 3, 80, 76, 84, 69,
                -1, 77, 0, 92, 53, 56, 127, 0, 0, 0,
                1, 116, 82, 78, 83, -52, -46, 52, 86, -3,
                0, 0, 0, 10, 73, 68, 65, 84, 120, -100,
                99, 98, 0, 0, 0, 6, 0, 3, 54, 55,
                124, -88, 0, 0, 0, 0, 73, 69, 78, 68, -82,
        };
        return Drawable.createFromStream(new ByteArrayInputStream(bts), null);
    }

    @Override
    public List<Issue> getOpenedIssues() throws IOException {
        return Arrays.asList(issue1, issue2);
    }

    @Override
    public List<Message> getIssueHistory(Issue issue) throws IOException {
        if (issue == issue1) {
            return messagesForIssue1;
        } else if (issue == issue2) {
            return messagesForIssue2;
        } else throw new IOException();
    }

    @Override
    public List<Message> getNewMessages(int issue_id, long timestamp) {
        return Collections.emptyList();
    }

    @Override
    public Issue createIssue(Issue issue) throws IOException {
        return issue;
    }

    @Override
    public void sendMessage(Message message) throws IOException {

    }

    @Override
    public Actor finger() throws IOException {
        return investor;
    }

    @Override
    public String getCookie(String username, String password) throws IOException {
        if (username.equals("user") && password.equals("password"))
            return "some stub cookie";
        throw new IOException("Wrong credentials provided");
    }
}
