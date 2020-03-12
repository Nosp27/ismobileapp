package com.pashikhmin.ismobileapp.cache;

import com.pashikhmin.ismobileapp.network.connectors.RESTConnector;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RESTCacheImpl implements RESTConnector {
    private RESTConnector underlying;
    private Map<String, String> cacheMap;

    public RESTCacheImpl(RESTConnector underlying) {
        cacheMap = new HashMap<>();
        this.underlying = underlying;
    }

    @Override
    public JSONTokener get(String path) throws IOException {
        if (!cacheMap.containsKey(path))
            cacheMap.put(path, underlying.get(path).nextTo((char)0));
        return new JSONTokener(cacheMap.get(path));
    }

    @Override
    public JSONTokener post(String path, String data) throws IOException {
        return underlying.post(path, data);
    }
}
