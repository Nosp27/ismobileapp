package com.pashikhmin.ismobileapp.cache;

import android.graphics.drawable.Drawable;
import com.pashikhmin.ismobileapp.network.Connectors;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DrawablesCache {
    static Map<Integer, Drawable> cacheMap;

    private static void init() {
        if (cacheMap == null) {
            cacheMap = new HashMap<>();
        }
    }

    public static Drawable getFromCache(int key) {
        init();
        if (cacheMap.containsKey(key))
            return cacheMap.get(key);
        return null;
    }

    public static void putToCache(Integer key, Drawable drawable) {
        init();
        if (key != null)
            cacheMap.put(key, drawable);
    }
}
