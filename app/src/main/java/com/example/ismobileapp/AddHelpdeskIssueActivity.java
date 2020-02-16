package com.example.ismobileapp;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.pashikhmin.ismobileapp.model.helpdesk.Issue;

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
            Intent resultIntent = new Intent();
            resultIntent.putExtra("issue", resultIssue);
            setResult(0, resultIntent);
            finish();
        } else {
            Toast.makeText(this, "Ivalid parameters for creating issue", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidIssue(Issue issue) {
        String issueTopic = issue.getTopic();
        ArrayList<String> issueTopics = getIntent().getStringArrayListExtra("issue_topics");
        return issueTopics == null || !issueTopics.contains(issueTopic);
    }
}
