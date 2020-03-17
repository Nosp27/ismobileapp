package com.pashikhmin.ismobileapp;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.pashikhmin.ismobileapp.model.Criteries;
import com.pashikhmin.ismobileapp.model.helpdesk.Actor;
import com.pashikhmin.ismobileapp.model.helpdesk.Issue;
import com.pashikhmin.ismobileapp.model.helpdesk.Message;
import com.pashikhmin.ismobileapp.network.connectors.Connectors;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTask;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTaskResult;
import com.pashikhmin.ismobileapp.resourceSupplier.ApiConnector;
import com.pashikhmin.ismobileapp.viewmodel.IssueListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HelpDeskActivity extends AppCompatActivity implements HeaderFragmentRequred {
    private static final String TAG = "HelpDeskActivity";
    private static final int ADD_ACTIVITY_REQUEST_CODE = 799;
    private ApiConnector resourceSupplier;
    private List<Issue> loadedIssues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        resourceSupplier = Connectors.api();
        LoadTask<List<Issue>> loadIssuesTask = new LoadTask<>(this::loadIssues, this::onIssuesLoaded);
        loadIssuesTask.execute();
    }

    /**
     * Async Method, dont call from Main thread
     *
     * @return User's issues
     */
    private List<Issue> loadIssues(Criteries... criteries) {
        try {
            return resourceSupplier.getOpenedIssues();
        } catch (IOException e) {
            Log.e(TAG, "Exception while loading issues", e);
            throw new RuntimeException(e);
        }
    }

    private List<Message> loadMessages(Issue issue) {
        try {
            List<Message> messages = resourceSupplier.getIssueHistory(issue);
            Actor me = resourceSupplier.finger();
            for (Message message : messages)
                message.setMine(me.getId() == message.getSenderId());
            return messages;
        } catch (IOException e) {
            Log.e(TAG, "Exception while loading message history", e);
            throw new RuntimeException(e);
        }
    }

    private void onMessagesLoaded(LoadTaskResult<List<Message>> taskResult) {
        if (!taskResult.successful()) {
            processTaskFail();
            return;
        }

        HashSet<Integer> myMessageIds = new HashSet<>();
        ArrayList<Message> messages = new ArrayList<>(taskResult.getResult());
        for(Message m : taskResult.getResult())
            if (m.isMine()) myMessageIds.add(m.getId());
        Intent transitIntent = new Intent(this, HelpDeskMessagesActivity.class);
        transitIntent.putExtra("my_message_ids", myMessageIds);
        transitIntent.putExtra("all_messages", messages);
        transitIntent.putExtra("issue", messages.get(0).getIssueId());
        startActivity(transitIntent);
    }

    private void onIssuesLoaded(LoadTaskResult<List<Issue>> taskResult) {
        if (!taskResult.successful()) {
            processTaskFail();
            return;
        }
        setContentView(R.layout.help_desk_issues);
        loadedIssues = new ArrayList<>(taskResult.getResult());
        findViewById(R.id.add_issue_btn).setOnClickListener(this::onAddIssueClick);
        setIssueListViewAdapter();
    }

    private void onIssueClick(Issue clicked) {
        setContentView(R.layout.layout_loading);
        LoadTask<List<Message>> loadMessagesTask = new LoadTask<>(
                e -> loadMessages(clicked), this::onMessagesLoaded
        );
        loadMessagesTask.execute();
    }

    private void onAddIssueClick(View view) {
        Intent startAddIssueIntent = new Intent(this, AddHelpdeskIssueActivity.class);
        ArrayList<String> issueTopics = new ArrayList<>();
        for (Issue issue : loadedIssues)
            issueTopics.add(issue.getTopic());
        startAddIssueIntent.putExtra("issue_topics", issueTopics);
        startActivityForResult(startAddIssueIntent, ADD_ACTIVITY_REQUEST_CODE);
    }

    private void processTaskFail() {
        setContentView(R.layout.error_screen);
        findViewById(R.id.retry_button).setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            throw new RuntimeException(
                    String.format("Activity with request code %d finished with non zero result (%d)",
                            requestCode, resultCode
                    )
            );
        }
        switch (requestCode) {
            case ADD_ACTIVITY_REQUEST_CODE:
                onIssueAdded((Issue) data.getSerializableExtra("issue"));
                break;
            default:
                break;
        }
    }

    private void onIssueAdded(Issue issue) {
        loadedIssues.add(issue);
        setIssueListViewAdapter();
    }

    private void setIssueListViewAdapter() {
        ((ListView) findViewById(R.id.helpdesk_issues)).setAdapter(
                new IssueListAdapter(this, R.layout.help_desk_issue, loadedIssues, this::onIssueClick)
        );
    }

    @Override
    public int resourceId(String tag) {
        return R.layout.header_fragment;
    }

    @Override
    public String topic(String tag) {
        return "issues";
    }
}
