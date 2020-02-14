package com.pashikhmin.ismobileapp.model.helpdesk;

import java.util.List;

public class Issue {
    private int id;
    private String status;
    private String topic;

    public Issue() {
    }

    public Issue(String topic) {
        setTopic(topic);
    }

    // <editor-fold desc="Accessors">
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
    // </editor-fold>
}
