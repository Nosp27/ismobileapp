package com.pashikhmin.ismobileapp;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.pashikhmin.ismobileapp.R;
import com.pashikhmin.ismobileapp.model.helpdesk.Issue;
import com.pashikhmin.ismobileapp.network.connectors.Connectors;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTask;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTaskResult;

import java.io.IOException;
import java.util.ArrayList;

public class AddHelpdeskIssueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpdesk_add_issue);
        Button submitButton = findViewById(R.id.add_issue_btn);
        submitButton.setOnClickListener(this::onConfirmAddIssue);
    }

    private void onConfirmAddIssue(View view) {
        String issueTopic = ((TextView) findViewById(R.id.issue_topic)).getText().toString();
        String issueDescription = ((TextView) findViewById(R.id.issue_topic)).getText().toString();

        Issue resultIssue = new Issue(issueTopic);
        if (isValidIssue(resultIssue)) {
            new LoadTask<>(
                    x -> {
                        try {
                            return Connectors.api().createIssue(resultIssue);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }, this::returnResult
            ).execute();
        } else {
            Toast.makeText(this, "Invalid parameters for creating issue", Toast.LENGTH_SHORT).show();
        }
    }

    private void returnResult(LoadTaskResult<Issue> resultIssue) {
        if(!resultIssue.successful()) {
            setContentView(R.layout.error_screen);
            return;
        }
        Intent resultIntent = new Intent();
        resultIntent.putExtra("issue", resultIssue.getResult());
        setResult(0, resultIntent);
        finish();
    }

    private boolean isValidIssue(Issue issue) {
        String issueTopic = issue.getTopic();
        ArrayList<String> issueTopics = getIntent().getStringArrayListExtra("issue_topics");
        return issueTopics == null || !issueTopics.contains(issueTopic);
    }
}
