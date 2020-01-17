package com.pashikhmin.ismobileapp.cache;

import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import com.pashikhmin.ismobileapp.model.*;
import com.pashikhmin.ismobileapp.resourceSupplier.BinaryDataProvider;
import com.pashikhmin.ismobileapp.resourceSupplier.ResourceSupplier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Cache implements ResourceSupplier, BinaryDataProvider {

    private AtomicBoolean cacheWarmFlag = new AtomicBoolean(false);

    private ResourceSupplier connector;

    private CachedEntities<Region> cachedRegions;
    private CachedEntities<Category> cachedCategories;
    private CachedEntities<Drawable> cachedDrawables;

    private BinaryDataProvider loadBinaryDataProvider;

    @Override
    public BinaryDataProvider getBinaryDataProvider() {
        return loadBinaryDataProvider;
    }

    @Override
    public void setBinaryDataProvider(BinaryDataProvider binaryDataProvider) {
        this.loadBinaryDataProvider = binaryDataProvider;
    }

    Cache(ResourceSupplier connector) throws IOException {
        this.connector = connector;

        // set cached image providing
        setBinaryDataProvider(connector.getBinaryDataProvider());
        connector.setBinaryDataProvider(this);
    }

    void checkOrWarmUpCaches() throws IOException {
        if (cacheWarmFlag.get())
            return;

        CachedEntities<Region> allRegions = downloadEntitiesToCache(connector.getAllRegions());
        CachedEntities<Category> allCategories = downloadEntitiesToCache(connector.getAllCategories());
        synchronized (this) {
            cachedRegions = allRegions;
            cachedCategories = allCategories;
            if (cachedDrawables == null)
                cachedDrawables = new CachedEntities<>();
            cacheWarmFlag.set(true);
        }
    }

    private <T extends Entity> CachedEntities<T> downloadEntitiesToCache(List<T> entities) {
        CachedEntities<T> cachedEntities = this.new CachedEntities<>();
        for (T entity : entities)
            cachedEntities.addEntityToMap(entity.getId(), entity);
        return cachedEntities;
    }

    @Override
    public List<Region> getAllRegions() throws IOException {
        checkOrWarmUpCaches();
        return cachedRegions.getAllEntities();
    }

    @Override
    public List<Category> getAllCategories() throws IOException {
        checkOrWarmUpCaches();
        return cachedCategories.getAllEntities();
    }

    @Override
    public List<Facility> getCriterizedFacilities(Criteries criteries) throws IOException {
        checkOrWarmUpCaches();
        return connector.getCriterizedFacilities(criteries);
    }

    @Override
    public Drawable loadImage(Integer imageId) throws IOException {
        if (cachedDrawables == null)
            cachedDrawables = new CachedEntities<>();
        if (imageId == null)
            return null;
        Drawable ret;
        ret = cachedDrawables.getEntityById(imageId);
        if (ret != null)
            return ret;
        ret = loadBinaryDataProvider.loadImage(imageId);
        cachedDrawables.addEntityToMap(imageId, ret);
        return ret;
    }

    void resetCaches() {
        cacheWarmFlag.set(false);
    }

    private class CachedEntities<T> {
        private SparseArray<T> cacheMap;

        CachedEntities() {
            cacheMap = new SparseArray<>();
        }

        void addEntityToMap(Integer key, T newEntity) {
            cacheMap.put(key, newEntity);
        }

        T getEntityById(Integer id) {
            synchronized (Cache.this) {
                if (id == null)
                    throw new RuntimeException("Id cannot be null");

                return cacheMap.get(id);
            }
        }

        List<T> getAllEntities() {
            synchronized (Cache.this) {
                ArrayList<T> ret = new ArrayList<>();
                for (int i = 0; i < cacheMap.size(); i++) {
                    ret.add(cacheMap.valueAt(i));
                }
                return ret;
            }
        }
    }
}
