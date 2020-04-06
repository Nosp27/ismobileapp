package com.pashikhmin.ismobileapp.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.pashikhmin.ismobileapp.R;
import com.pashikhmin.ismobileapp.model.helpdesk.Message;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessagesListAdapter extends ArrayAdapter<Message> {
    protected int myMessageResource;
    protected int otherMessageResource;
    List<Message> messages;

    public MessagesListAdapter(
            int myMessageResource,
            int otherMessageResource,
            Context context,
            int resource,
            List<Message> messages) {
        super(context, resource, messages);
        this.myMessageResource = myMessageResource;
        this.otherMessageResource = otherMessageResource;
        this.messages = messages;
    }

    @NotNull
    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        Message message = messages.get(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(
                    myMessageResource,
                    parent,
                    false);

        TextView bubble = convertView.findViewById(R.id.bubble);
        ConstraintLayout.LayoutParams lp = ((ConstraintLayout.LayoutParams) bubble.getLayoutParams());
        if(message.getId() == 879)
            convertView.setClickable(false);
        if (message.getMine()) {
            lp.endToEnd = 0;
            lp.startToStart = -1;
        } else {
            lp.startToStart = 0;
            lp.endToEnd = -1;
        }
        bubble.setLayoutParams(lp);
        bubble.setText(message.getContent());
        convertView.setClickable(false);
        return convertView;
    }
}
