package com.pashikhmin.ismobileapp.cache;

import com.pashikhmin.ismobileapp.network.connectors.HttpConnector;
import com.pashikhmin.ismobileapp.network.connectors.Redirect;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.*;

public class HttpCacheImpl implements HttpConnector {
    HttpConnector underlying;
    Map<String, byte[]> cacheMapGET;
    Map<String, byte[]> cacheMapPOST;

    List<String> cacheableGets = Arrays.asList(
            "/image/"
    );

    public HttpCacheImpl(HttpConnector underlying) {
        cacheMapGET = new HashMap<>();
        cacheMapPOST = new HashMap<>();
        this.underlying = underlying;
    }

    @Override
    public InputStream connect(String path, String data, Redirect redirect) throws IOException {
        boolean caching = false;
        if (data == null)
            for (String tag : cacheableGets)
                if (path.contains(tag)) {
                    caching = true;
                    break;
                }
        if (caching) {
            Map<String, byte[]> mapTocheck = data == null ? cacheMapGET : cacheMapPOST;
            if (!mapTocheck.containsKey(path)) {
                InputStream loaded = underlying.connect(path, data, redirect);
                byte[] contentRead = readStream(loaded);
                mapTocheck.put(path, contentRead);
            }
            return new ByteArrayInputStream(mapTocheck.get(path));
        }
        return underlying.connect(path, data, redirect);
    }

    @Override
    public Map<String, List<String>> getHeaderFields(String path, String data, Redirect redirect) throws IOException {
        return underlying.getHeaderFields(path, data, redirect);
    }

    byte[] readStream(InputStream is) throws IOException {
        ArrayList<Byte> buffer = new ArrayList<>();
        int b;
        while((b = is.read()) >= 0) {
            buffer.add((byte)b);
        }

        byte[] ret = new byte[buffer.size()];
        for(int i = 0; i < buffer.size(); i++)
            ret[i] = buffer.get(i);
        return ret;
    }
}
