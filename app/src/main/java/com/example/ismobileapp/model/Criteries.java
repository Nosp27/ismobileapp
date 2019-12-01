package com.example.ismobileapp.model;

import com.example.ismobileapp.network.json.JSONField;

import java.io.Serializable;

public class Criteries implements Serializable {
    @JSONField
    public Integer[] regions;
    @JSONField
    public String[] categories;
}
