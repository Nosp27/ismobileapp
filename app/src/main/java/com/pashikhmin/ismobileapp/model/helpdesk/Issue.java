package com.pashikhmin.ismobileapp.model.helpdesk;

import com.pashikhmin.ismobileapp.network.json.JSONField;

import java.io.Serializable;
import java.util.List;

public class Issue implements Serializable {
    @JSONField
    private int id;
    @JSONField
    private String status;
    @JSONField
    private String topic;

    public Issue() {
    }

    public Issue(String topic) {
        setTopic(topic);
    }

    // <editor-fold desc="Accessors">
    public int getId() {
        return id;
    }

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
