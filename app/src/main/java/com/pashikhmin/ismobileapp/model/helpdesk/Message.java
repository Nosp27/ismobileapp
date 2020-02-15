package com.pashikhmin.ismobileapp.model.helpdesk;

import com.pashikhmin.ismobileapp.network.json.JSONField;

public class Message {
    int id;
    @JSONField
    String content;
}
