package com.pashikhmin.ismobileapp.model.helpdesk;

import com.pashikhmin.ismobileapp.network.json.JSONField;

import java.io.Serializable;

public class Actor implements Serializable {
    @JSONField
    private int id;
    @JSONField
    private String username;

    public Actor() {
    }

    public Actor(String username) {
        this.username = username;
        this.id = username.hashCode();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
