package com.pashikhmin.ismobileapp.model.helpdesk;

import com.pashikhmin.ismobileapp.network.json.JSONField;

import java.io.Serializable;

public class Message implements Serializable {
    @JSONField
    private int id;
    @JSONField
    private String content;
    @JSONField
    private Actor actor;
    @JSONField
    private Issue issue;

    private transient Boolean isMine;

    public Message() {
    }

    public Message(Issue issue, String content) {
        this.issue = issue;
        this.content = content;
    }

    public Message(String content, Actor actor) {
        this.content = content;
        this.actor = actor;
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
        return actor.getId();
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
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
