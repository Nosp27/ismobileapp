package com.pashikhmin.ismobileapp.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
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
                    message.isMine() ? myMessageResource : otherMessageResource,
                    parent,
                    false);

        ((TextView) convertView.findViewById(R.id.bubble)).setText(message.getContent());
        convertView.setClickable(false);
        return convertView;
    }
}
