package com.pashikhmin.ismobileapp;

public interface HeaderFragmentRequred {
    int resourceId(String tag);
    String topic(String tag);
    default String userName(){
        return "User Name";
    }
}
