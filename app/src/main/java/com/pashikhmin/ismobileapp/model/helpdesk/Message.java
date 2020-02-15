package com.pashikhmin.ismobileapp.model.helpdesk;

import com.pashikhmin.ismobileapp.network.json.JSONField;

import java.io.Serializable;

public class Message implements Serializable {
    @JSONField
    private int id;
    @JSONField
    private String content;
    @JSONField
    private Actor sender;
    @JSONField
    private Issue issue;

    private transient Boolean isMine;

    public Message() {
    }

    public Message(String content, Actor sender) {
        this.content = content;
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSenderId() {
        return sender.getId();
    }

    public Actor getSender() {
        return sender;
    }

    public void setSender(Actor sender) {
        this.sender = sender;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public Boolean isMine() {
        return isMine;
    }

    public void setMine(Boolean mine) {
        isMine = mine;
    }
}
