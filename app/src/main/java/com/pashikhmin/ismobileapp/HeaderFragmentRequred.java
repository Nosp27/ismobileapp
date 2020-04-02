package com.pashikhmin.ismobileapp;

import com.pashikhmin.ismobileapp.network.connectors.Connectors;

import java.io.IOException;

public interface HeaderFragmentRequred {
    int resourceId(String tag);
    String topic(String tag);
    default String userName(){
        String unauthorizedName = "guest";
        try {
            return Connectors.userAuthorized() ? Connectors.api().finger().getFullName() : unauthorizedName;
        } catch (IOException e) {
            return unauthorizedName;
        }
    }
}
