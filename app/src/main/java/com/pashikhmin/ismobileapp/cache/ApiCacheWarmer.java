package com.pashikhmin.ismobileapp.cache;

import com.pashikhmin.ismobileapp.cache.CacheWarmer;
import com.pashikhmin.ismobileapp.model.Category;
import com.pashikhmin.ismobileapp.model.Entity;
import com.pashikhmin.ismobileapp.model.Region;
import com.pashikhmin.ismobileapp.network.connectors.HttpConnector;
import com.pashikhmin.ismobileapp.network.connectors.RESTConnector;
import com.pashikhmin.ismobileapp.network.connectors.Redirect;
import com.pashikhmin.ismobileapp.network.json.JSONParser;
import com.pashikhmin.ismobileapp.resourceSupplier.ApiConnector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ApiCacheWarmer implements CacheWarmer {
    private AtomicBoolean cacheWarmFlag = new AtomicBoolean(false);
    HttpConnector http;
    RESTConnector rest;
    JSONParser parser;

    public ApiCacheWarmer(
            HttpConnector http,
            RESTConnector rest,
            JSONParser parser
    ) {
        this.http = http;
        this.rest = rest;
        this.parser = parser;
    }

    @Override
    public synchronized void warmUp() throws IOException {
        if (cacheWarmFlag.get())
            return;

        // warm up
        List<Entity> cachedEntities = new ArrayList<>();
        cachedEntities.addAll(
                parser.readList(
                        Region.class, rest.get(ApiConnector.GET_ALL_REGIONS)
                )
        );
        cachedEntities.addAll(
                parser.readList(
                        Category.class, rest.get(ApiConnector.GET_ALL_CATEGORIES)
                )
        );

        for (Entity e : cachedEntities) {
            int imageId = e.getImageId();
            http.connect(
                    ApiConnector.SERVER + ApiConnector.READ_IMAGE_SUFFIX + imageId,
                    null,
                    Redirect.FOLLOW
            );
        }

        cacheWarmFlag.set(true);
    }

    @Override
    public synchronized void reset() {
        cacheWarmFlag.set(false);
    }
}
