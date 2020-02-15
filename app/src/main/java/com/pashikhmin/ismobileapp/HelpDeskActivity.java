package com.pashikhmin.ismobileapp;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.ismobileapp.R;
import com.pashikhmin.ismobileapp.model.Criteries;
import com.pashikhmin.ismobileapp.model.helpdesk.Issue;
import com.pashikhmin.ismobileapp.network.Connectors;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTask;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTaskResult;
import com.pashikhmin.ismobileapp.resourceSupplier.HelpDeskResourceSupplier;
import com.pashikhmin.ismobileapp.resourceSupplier.ResourceSupplier;

import java.io.IOException;
import java.util.List;

public class HelpDeskActivity extends AppCompatActivity {
    private static final String TAG = "HelpDeskActivity";
    private HelpDeskResourceSupplier resourceSupplier;

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

    private void onIssuesLoaded(LoadTaskResult<List<Issue>> taskResult) {
        if(!taskResult.successful()) {
            setContentView(R.layout.error_screen);
            findViewById(R.id.retry_button).setVisibility(View.INVISIBLE);
            return;
        }
        List<Issue> result = taskResult.getResult();
        LinearLayout messageContainer = findViewById(R.id.helpdesk_messages);
        setContentView(R.layout.help_desk);
        for(Issue issue : result) {
            View messageView = getLayoutInflater().inflate(R.layout.help_desk_message_left, messageContainer);
            fillIssueListItem(messageView, issue);
        }
    }

    private void fillIssueListItem(View listItem, Issue issue) {
        ((TextView)listItem.findViewById(R.id.bubble)).setText(issue.getTopic());
    }
}
