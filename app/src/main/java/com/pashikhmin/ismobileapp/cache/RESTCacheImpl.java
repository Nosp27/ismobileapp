package com.pashikhmin.ismobileapp.cache;

import com.pashikhmin.ismobileapp.network.connectors.RESTConnector;
import org.hamcrest.Matcher;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RESTCacheImpl implements RESTConnector {
    private RESTConnector underlying;
    private Map<String, String> cacheMap;

    private Matcher<String> getMatcher;
    private Matcher<String> postMatcher;

    private CacheWarmer warmer;

    public RESTCacheImpl(RESTConnector underlying, CacheBuilder builder) {
        if (builder != null) {
            getMatcher = builder.getMatcher;
            postMatcher = builder.postMatcher;
            warmer = builder.warmer;
        }

        cacheMap = new HashMap<>();
        this.underlying = underlying;
    }

    @Override
    public JSONTokener get(String path) throws IOException {
        if (!getMatcher.matches(path))
            return new JSONTokener(underlying.get(path).nextTo((char) 0));
        if (!cacheMap.containsKey(path))
            cacheMap.put(path, underlying.get(path).nextTo((char) 0));
        return new JSONTokener(cacheMap.get(path));
    }

    @Override
    public JSONTokener post(String path, String data) throws IOException {
        return underlying.post(path, data);
    }
}
