package com.pashikhmin.ismobileapp.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.pashikhmin.ismobileapp.R;
import com.pashikhmin.ismobileapp.model.helpdesk.Message;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.List;

public class MessagesListAdapter extends ArrayAdapter<Message> {
    protected int messageResource;
    List<Message> messages;

    public MessagesListAdapter(
            Context context,
            int resource,
            List<Message> messages) {
        super(context, resource, messages);
        this.messageResource = resource;
        this.messages = messages;
    }

    @NotNull
    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        Message message = messages.get(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(
                    messageResource,
                    parent,
                    false);

        TextView bubble = convertView.findViewById(R.id.bubble);
        TextView date = convertView.findViewById(R.id.timestamp);
        ConstraintLayout.LayoutParams bubbleLP = ((ConstraintLayout.LayoutParams) bubble.getLayoutParams());
        ConstraintLayout.LayoutParams timeLP = ((ConstraintLayout.LayoutParams) date.getLayoutParams());
        if (message.getId() == 879)
            convertView.setClickable(false);
        if (message.getMine()) {

            timeLP.endToEnd = bubbleLP.endToEnd = 0;
            timeLP.startToStart = bubbleLP.startToStart = -1;
        } else {
            timeLP.startToStart = bubbleLP.startToStart = 0;
            timeLP.endToEnd = bubbleLP.endToEnd = -1;
        }

        bubble.setLayoutParams(bubbleLP);
        bubble.setText(message.getContent());

        date.setLayoutParams(timeLP);
        date.setText(message.getTimestampFormatted());

        convertView.setClickable(false);
        return convertView;
    }
}
