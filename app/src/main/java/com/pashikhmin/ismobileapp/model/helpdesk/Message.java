package com.pashikhmin.ismobileapp.model.helpdesk;

import com.pashikhmin.ismobileapp.network.json.JSONField;

import java.io.Serializable;

public class Message implements Serializable {
    @JSONField
    private int id;
    @JSONField
    private String content;
    @JSONField
    private int actorId;
    @JSONField
    private int issueId;

    private transient Boolean isMine;

    public Message() {
    }

    public Message(int issueId, String content) {
        this.issueId = issueId;
        this.content = content;
    }

    public Message(String content, Actor actor) {
        this.content = content;
        this.actorId = actor.getId();
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

    public int getSenderId() {
        return actorId;
    }

    public int getIssueId() {
        return issueId;
    }
    
    public Boolean isMine() {
        return isMine;
    }

    public void setMine(Boolean mine) {
        isMine = mine;
    }
}
