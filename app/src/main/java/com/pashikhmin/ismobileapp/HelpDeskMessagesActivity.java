package com.pashikhmin.ismobileapp;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.pashikhmin.ismobileapp.model.helpdesk.Message;
import com.pashikhmin.ismobileapp.network.connectors.Connectors;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTask;
import com.pashikhmin.ismobileapp.network.loadTask.MessageHistoryUpdater;
import com.pashikhmin.ismobileapp.viewmodel.MessagesListAdapter;

import java.io.IOException;
import java.util.*;

public class HelpDeskMessagesActivity extends AppCompatActivity implements HeaderFragmentRequred {
    int issue_id;
    MessageHistoryUpdater updater;
    Handler messageUpdateHandler;
    List<Message> localMessageHistory;
    MessagesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_desk_messages);
        Intent intent = getIntent();
        issue_id = intent.getIntExtra("issue", -1);
        localMessageHistory = (ArrayList<Message>) intent.getSerializableExtra("all_messages");
        localMessageHistory.sort(Comparator.comparing(Message::getSendTime));
        if (issue_id == -1)
            throw new RuntimeException("No issue provided");
        if (localMessageHistory == null)
            throw new RuntimeException("Null message history provided");
        updater = new MessageHistoryUpdater(localMessageHistory, issue_id);
        messageUpdateHandler = new Handler();
        updater.setOnUpdate(() -> messageUpdateHandler.postAtFrontOfQueue(
                () -> renderMessages(updater.getUpToDateHistory())
        ));
        renderMessages(localMessageHistory);
        findViewById(R.id.send_message).setOnClickListener(this::onSendClick);
    }

    private void renderMessages(List<Message> messageHistory) {
        ListView messageContainer = findViewById(R.id.helpdesk_messages);
        if (messageContainer.getAdapter() != null
                && messageHistory.size() == messageContainer.getAdapter().getCount()
        )
            return;
        localMessageHistory.clear();
        localMessageHistory.addAll(messageHistory);
        if (adapter == null) {
            adapter = new MessagesListAdapter(
                    this,
                    R.layout.help_desk_message_right,
                    localMessageHistory
            );
            messageContainer.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        messageContainer.smoothScrollToPosition(adapter.getCount() - 1);
    }

    private void onSendClick(View view) {
        EditText messageField = findViewById(R.id.helpdesk_message_input);
        String messageText = messageField.getText().toString();
        messageField.setText("");

        Message submittedMessage = new Message(issue_id, messageText);
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
                    if (!loadTaskResult.successful())
                        throw new RuntimeException(loadTaskResult.getError());
                }
        ).execute();
    }

    @Override
    public int resourceId(String tag) {
        return R.layout.header_fragment;
    }

    @Override
    public String topic(String tag) {
        return getResources().getString(R.string.messages);
    }

    @Override
    protected void onDestroy() {
        updater.stop();
        super.onDestroy();
    }
}
