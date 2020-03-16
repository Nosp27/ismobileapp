package com.pashikhmin.ismobileapp;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.pashikhmin.ismobileapp.model.helpdesk.Message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HelpDeskMessagesActivity extends AppCompatActivity implements HeaderFragmentRequred  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_desk_messages);

        Intent intent = getIntent();
        List<Message> messageHistory = (ArrayList<Message>)intent.getSerializableExtra("all_messages");
        Set<Integer> myMessageIds = (HashSet<Integer>)intent.getSerializableExtra("my_message_ids");
        if(messageHistory == null)
            throw new RuntimeException("Null message history provided");
        if(myMessageIds == null)
            throw new RuntimeException("Null my message ids provided");
        for (Message m : messageHistory)
            m.setMine(myMessageIds.contains(m.getId()));
        loadMessages(messageHistory);
    }

    private void loadMessages(List<Message> messageHistory) {
        LinearLayout messageContainer = findViewById(R.id.helpdesk_messages);
        for (Message message : messageHistory) {
            View messageView = getLayoutInflater().inflate(
                    message.isMine() ? R.layout.help_desk_message_right : R.layout.help_desk_message_left, null
            );
            messageContainer.addView(messageView);
            fillMessageListItem(messageView, message);
        }
    }

    private void fillMessageListItem(View listItem, Message message) {
        ((TextView) listItem.findViewById(R.id.bubble)).setText(message.getContent());
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
