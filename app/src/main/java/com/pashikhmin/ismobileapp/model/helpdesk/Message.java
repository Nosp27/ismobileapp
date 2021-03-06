package com.pashikhmin.ismobileapp.model.helpdesk;

import android.icu.text.SimpleDateFormat;
import com.pashikhmin.ismobileapp.network.json.JSONField;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Message implements Serializable {
    @JSONField
    private int id;
    @JSONField
    private String content;
    @JSONField
    private boolean mine;
    @JSONField
    private int issueId;
    @JSONField
    private long sendTime;

    public Message() {
    }

    public Message(int issueId, String content) {
        this.issueId = issueId;
        this.content = content;
        this.sendTime = System.currentTimeMillis();
        mine = true;
    }

    // for testing
    public Message(String content, boolean mine) {
        this.content = content;
        this.mine = mine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIssueId() {
        return issueId;
    }

    public boolean getMine() {
        return mine;
    }

    public long getSendTime() {
        return sendTime;
    }

    public String getTimestampFormatted() {
        String pattern = "dd MMM HH:mm";
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat(pattern, Locale.getDefault());
        return simpleDateFormat.format(new Date(sendTime));
    }
}
