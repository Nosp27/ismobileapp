package com.pashikhmin.ismobileapp;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.ismobileapp.AddHelpdeskIssueActivity;
import com.example.ismobileapp.R;
import com.pashikhmin.ismobileapp.model.Criteries;
import com.pashikhmin.ismobileapp.model.helpdesk.Actor;
import com.pashikhmin.ismobileapp.model.helpdesk.Issue;
import com.pashikhmin.ismobileapp.model.helpdesk.Message;
import com.pashikhmin.ismobileapp.network.Connectors;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTask;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTaskResult;
import com.pashikhmin.ismobileapp.resourceSupplier.HelpDeskResourceSupplier;
import com.pashikhmin.ismobileapp.viewmodel.IssueListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelpDeskActivity extends AppCompatActivity {
    private static final String TAG = "HelpDeskActivity";
    private static final int ADD_ACTIVITY_REQUEST_CODE = 799;
    private HelpDeskResourceSupplier resourceSupplier;
    private List<Issue> loadedIssues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        resourceSupplier = (HelpDeskResourceSupplier) Connectors.getDefaultCachedConnector();
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

    private List<Message> loadMessages(Issue issue, Criteries... criteries) {
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
        setContentView(R.layout.help_desk_messages);
        List<Message> result = taskResult.getResult();
        LinearLayout messageContainer = findViewById(R.id.helpdesk_messages);
        for (Message message : result) {
            View messageView = getLayoutInflater().inflate(
                    message.isMine() ? R.layout.help_desk_message_right : R.layout.help_desk_message_left, null
            );
            messageContainer.addView(messageView);
            fillMessageListItem(messageView, message);
        }
    }

    private void onIssuesLoaded(LoadTaskResult<List<Issue>> taskResult) {
        if (!taskResult.successful()) {
            processTaskFail();
            return;
        }
        setContentView(R.layout.help_desk_issues);
        loadedIssues = taskResult.getResult();
        findViewById(R.id.add_issue_btn).setOnClickListener(this::onAddIssueClick);
        setIssueListViewAdapter();
    }

    private void onIssueClick(Issue clicked) {
        setContentView(R.layout.layout_loading);
        LoadTask<List<Message>> loadMessagesTask = new LoadTask<>(
                e -> loadMessages(clicked, e), this::onMessagesLoaded
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

    private void fillMessageListItem(View listItem, Message message) {
        ((TextView) listItem.findViewById(R.id.bubble)).setText(message.getContent());
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
        // TODO: add async task for uploading issue
        loadedIssues.add(issue);
        setIssueListViewAdapter();
    }

    private void setIssueListViewAdapter() {
        ((ListView) findViewById(R.id.helpdesk_issues)).setAdapter(
                new IssueListAdapter(this, R.layout.helpdesk_issue, loadedIssues, this::onIssueClick)
        );
    }
}
