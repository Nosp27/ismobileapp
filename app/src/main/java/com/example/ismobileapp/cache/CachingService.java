package com.example.ismobileapp.cache;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.IBinder;
import android.util.SparseArray;
import com.example.ismobileapp.model.*;
import com.example.ismobileapp.resourceSupplier.CannotServeException;
import com.example.ismobileapp.resourceSupplier.ResourceSupplier;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CachingService extends Service {
    private ResourceSupplier connector;

    private CachedEntities<Region> cachedRegions;
    private CachedEntities<Category> cachedCategories;
    CachedEntities<Drawable> cachedDrawables;

    @Override
    public IBinder onBind(Intent intent) {
        return new CacheBinder();
    }

    public void downLoadCaches() throws IOException {
        cachedRegions = downloadEntitiesToCache(connector.getAllRegions());
        cachedCategories = downloadEntitiesToCache(connector.getAllCategories());
        if (cachedDrawables == null)
            cachedDrawables = new CachedEntities<>();
    }

    private <T extends Entity> CachedEntities<T> downloadEntitiesToCache(List<T> entities) {
        CachedEntities<T> cachedEntities = new CachedEntities<>();
        for (T entity : entities)
            cachedEntities.addEntityToMap(entity.getId(), entity);
        return cachedEntities;
    }

    public class CacheBinder extends android.os.Binder implements ResourceSupplier {
        CachingService cacheService;

        CacheBinder() {
            cacheService = CachingService.this;
        }

        @Override
        public List<Region> getAllRegions() throws IOException {
            return cachedRegions.getAllEntities();
        }

        @Override
        public List<Category> getAllCategories() throws IOException {
            return cachedCategories.getAllEntities();
        }

        @Override
        public List<Facility> getCriterizedFacilities(Criteries criteries) throws IOException {
            throw new CannotServeException();
        }

        @Override
        public InputStream loadImage(Integer key) throws IOException {
            return null;
        }
    }
}

class CachedEntities<T> {
    private SparseArray<T> cacheMap;

    CachedEntities() {
        cacheMap = new SparseArray<>();
    }

    void addEntityToMap(Integer key, T newEntity) {
        cacheMap.put(key, newEntity);
    }

    public T getEntityById(Integer id) {
        if (id == null)
            throw new RuntimeException("Id cannot be null");

        return cacheMap.get(id);
    }

    public List<T> getAllEntities() {
        ArrayList<T> ret = new ArrayList<>();
        for (int i = 0; i < cacheMap.size(); i++) {
            ret.add(cacheMap.valueAt(i));
        }
        return ret;
    }
}
