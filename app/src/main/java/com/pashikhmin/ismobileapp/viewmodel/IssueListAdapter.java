package com.pashikhmin.ismobileapp.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.pashikhmin.ismobileapp.R;
import com.pashikhmin.ismobileapp.model.helpdesk.Issue;

import java.util.List;
import java.util.function.Consumer;

public class IssueListAdapter extends ArrayAdapter<Issue> {
    protected int resource;
    protected List<Issue> issues;
    protected Consumer<Issue> onIssueClick;


    public IssueListAdapter(Context context, int resource, List<Issue> content, Consumer<Issue> onIssueClick) {
        super(context, resource, content);
        this.resource = resource;
        this.issues = content;
        this.onIssueClick = onIssueClick;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);

        Issue issue = issues.get(position);
        ((TextView) convertView.findViewById(R.id.topic)).setText(issue.getTopic());
        ((TextView) convertView.findViewById(R.id.status)).setText(issue.getStatus());
        convertView.setOnClickListener(x -> onIssueClick.accept(issue));
        return convertView;
    }
}
