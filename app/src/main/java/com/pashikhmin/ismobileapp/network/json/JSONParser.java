package com.pashikhmin.ismobileapp.network.json;

import org.json.JSONTokener;

import java.util.List;

public interface JSONParser {
    <T> T readObject(Class<T> cls, JSONTokener tokener);
    <T> List<T> readList(Class<T> cls, JSONTokener tokener);
}
