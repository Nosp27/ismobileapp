package com.pashikhmin.ismobileapp;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.pashikhmin.ismobileapp.model.helpdesk.Issue;
import com.pashikhmin.ismobileapp.model.helpdesk.Message;
import com.pashikhmin.ismobileapp.network.connectors.Connectors;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTask;
import com.pashikhmin.ismobileapp.viewmodel.MessagesListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HelpDeskMessagesActivity extends AppCompatActivity implements HeaderFragmentRequred  {
    int issue_id;
    List<Message> messageHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_desk_messages);

        Intent intent = getIntent();
        issue_id = intent.getIntExtra("issue", -1);
        messageHistory = (ArrayList<Message>)intent.getSerializableExtra("all_messages");
        Set<Integer> myMessageIds = (HashSet<Integer>)intent.getSerializableExtra("my_message_ids");
        if(issue_id == -1)
            throw new RuntimeException("No issue provided");
        if(messageHistory == null)
            throw new RuntimeException("Null message history provided");
        if(myMessageIds == null)
            throw new RuntimeException("Null my message ids provided");
        for (Message m : messageHistory)
            m.setMine(myMessageIds.contains(m.getId()));
        renderMessages(messageHistory);
        findViewById(R.id.send_message).setOnClickListener(this::onSendClick);
    }

    private void renderMessages(List<Message> messageHistory) {
        ListView messageContainer = findViewById(R.id.helpdesk_messages);
        MessagesListAdapter adapter = new MessagesListAdapter(
                R.layout.help_desk_message_right,
                R.layout.help_desk_message_left,
                this,
                R.layout.help_desk_message_right,
                messageHistory
        );
        messageContainer.setAdapter(adapter);
    }

    private void onSendClick(View view) {
        String messageText = ((EditText)findViewById(R.id.helpdesk_message_input)).getText().toString();
        Message submittedMessage = new Message(issue_id, messageText);
        submittedMessage.setMine(true);
        new LoadTask<>(
                x -> {
                    try {
                        Connectors.api().sendMessage(submittedMessage);
                        return true;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                loadTaskResult -> {
                    if (loadTaskResult.successful()) {
                        messageHistory.add(submittedMessage);
                        renderMessages(messageHistory);
                    } else throw new RuntimeException(loadTaskResult.getError());
                }
        ).execute();
    }

    @Override
    public int resourceId(String tag) {
        return R.layout.header_fragment;
    }

    @Override
    public String topic(String tag) {
        return "Messages";
    }
}
