package com.example.ismobileapp.network.json;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class JSONModeller {
    private static final String TAG = "JSONModeller";

    public static <T> T fromJSON(Class<T> neededClass, JSONObject jsonObject) {
        try {
            T ret = neededClass.newInstance();
            for (Field field : neededClass.getFields())
                if (field.isAnnotationPresent(JSONField.class)) {
                    Object field_value = jsonObject.get(field.getName());
                    if (field_value == JSONObject.NULL)
                        field.set(ret, null);
                    else {
                        field.set(ret, jsonObject.get(field.getName()));
                        String processMethod = field.getAnnotation(JSONField.class).processResultMethod();
                        if (!processMethod.isEmpty()) {
                            Method process = neededClass.getMethod(processMethod);
                            process.invoke(ret);
                        }
                    }
                }
            return ret;
        } catch (IllegalAccessException | InstantiationException | JSONException e) {
            Log.e(TAG, e.toString());
            return null;
        } catch (NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
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
            return new JSONObject(map);
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
}
