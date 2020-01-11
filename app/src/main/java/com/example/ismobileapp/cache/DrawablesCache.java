package com.example.ismobileapp.cache;

import android.graphics.drawable.Drawable;
import com.example.ismobileapp.network.Connectors;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public static Drawable processImage(Integer imageId, String imageName) {
        if (imageId == null)
            return null;
        Drawable ret;
        ret = getFromCache(imageId);
        if (ret != null)
            return ret;
        try (InputStream is = Connectors.getDefaultConnector().loadImage(imageId)) {
            ret = Drawable.createFromStream(is, imageName);
            putToCache(imageId, ret);
            return ret;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
