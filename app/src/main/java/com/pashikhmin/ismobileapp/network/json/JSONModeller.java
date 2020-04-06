package com.pashikhmin.ismobileapp.network.json;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.*;
import java.util.*;

public class JSONModeller implements JSONParser{
    private static final String TAG = "JSONModeller";

    static <T> T fromJSON(Class<T> neededClass, JSONObject jsonObject) {
        try {
            T ret = neededClass.newInstance();
            for (Field field : neededClass.getDeclaredFields())
                if (field.isAnnotationPresent(JSONField.class)) {
                    field.setAccessible(true);
                    Object field_value = jsonObject.get(field.getName());
                    if (field_value == JSONObject.NULL)
                        field.set(ret, null);
                    else {
                        if (field_value instanceof JSONArray)
                            field_value = fromJSON(field.getType().getComponentType(), (JSONArray) field_value);
                        else if (field_value instanceof JSONObject && ((JSONObject) field_value).length() > 1)
                            field_value = fromJSON(field.getType(), (JSONObject) field_value);

                        String processMethod = field.getAnnotation(JSONField.class).processResultMethod();
                        if (!processMethod.isEmpty()) {
                            Method process = neededClass.getMethod(processMethod, Object.class);
                            field.set(ret, process.invoke(ret, field_value));
                        } else
                            field.set(ret, field_value);
                    }
                }
            return ret;
        } catch (IllegalAccessException | InstantiationException | JSONException e) {
            Log.e(TAG, e.toString());
            throw new RuntimeException(e);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    static <T> T[] fromJSON(Class<T> neededClass, JSONArray jsonArray) {
        try {
            T[] ret = (T[]) Array.newInstance(neededClass, jsonArray.length());
            for (int i = 0; i < ret.length; ++i) {
                Object element = jsonArray.get(i);
                ret[i] = fromJSON(neededClass, (JSONObject) element);
            }
            return ret;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T readObject(Class<T> cls, JSONTokener tokener) {
        if (tokener == null)
            return null;

        try {
            return fromJSON(cls, (JSONObject) tokener.nextValue());
        } catch (JSONException e) {
            e.printStackTrace();
            // TODO: 01/12/2019 handle json exception
            return null;
        }
    }

    public <T> List<T> readList(Class<T> cls, JSONTokener tokener) {
        if (tokener == null)
            throw new IllegalArgumentException("Tokener is null");
        try {
            JSONArray nextTokens = ((JSONArray) tokener.nextValue());
            T[] x = fromJSON(cls, nextTokens);
            return Arrays.asList(x);
        } catch (JSONException e) {
            throw new IllegalArgumentException("Parsing exception", e);
        }
    }

    public static JSONObject toJSON(Object object) {
        Map<String, Object> map = new HashMap<>();
        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
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
