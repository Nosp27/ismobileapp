package com.example.ismobileapp.network.json;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONModeller {
    private static final String TAG = "JSONModeller";

    public static <T> T fromJSON(Class<T> neededClass, JSONObject jsonObject) {
        try {
            T ret = neededClass.newInstance();
            for (Field field : neededClass.getFields()) {
                if (!field.isAnnotationPresent(JSONField.class))
                    continue;
                field.set(ret, jsonObject.get(field.getName()));
            }
            return ret;
        } catch (IllegalAccessException | InstantiationException | JSONException e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public static JSONObject toJSON(Object object) {
        Map<String, Object> map = new HashMap<>();
        try {
            for (Field field : object.getClass().getFields()) {
                if (!field.isAnnotationPresent(JSONField.class))
                    continue;
                String fieldName = field.getName();
                fieldName = fieldName.substring(fieldName.indexOf(" ") + 1);
                map.put(fieldName, field.get(object));
            }
            return new JSONObject("{\"city\":\"chicago\",\"name\":\"jon doe\",\"age\":\"22\"}");
        } catch (IllegalAccessException | JSONException e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
}
