package com.pashikhmin.ismobileapp.model;

import com.pashikhmin.ismobileapp.network.json.JSONField;

import java.io.Serializable;

public class Criteries implements Serializable {
    @JSONField
    public Integer[] regions;
    @JSONField
    public Integer[] categories;

    public boolean onlyLiked;
    public boolean onlyUpdates;
}
