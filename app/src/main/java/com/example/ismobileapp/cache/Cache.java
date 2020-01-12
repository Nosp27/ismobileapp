package com.example.ismobileapp.cache;

import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import com.example.ismobileapp.model.*;
import com.example.ismobileapp.resourceSupplier.ResourceSupplier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Cache implements ResourceSupplier {

    private ResourceSupplier connector;

    private CachedEntities<Region> cachedRegions;
    private CachedEntities<Category> cachedCategories;
    private CachedEntities<Drawable> cachedDrawables;

    Cache(ResourceSupplier connector) throws IOException {
        this.connector = connector;
    }

    void warmUpCaches() throws IOException {
        CachedEntities<Region> allRegions = downloadEntitiesToCache(connector.getAllRegions());
        CachedEntities<Category> allCategories = downloadEntitiesToCache(connector.getAllCategories());
        synchronized (this) {
            cachedRegions = allRegions;
            cachedCategories = allCategories;
            if (cachedDrawables == null)
                cachedDrawables = new CachedEntities<>();
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
        return cachedRegions.getAllEntities();
    }

    @Override
    public List<Category> getAllCategories() throws IOException {
        return cachedCategories.getAllEntities();
    }

    @Override
    public List<Facility> getCriterizedFacilities(Criteries criteries) throws IOException {
        return connector.getCriterizedFacilities(criteries);
    }

    @Override
    public Drawable loadImage(Integer imageId) throws IOException {
        if (imageId == null)
            return null;
        Drawable ret;
        ret = cachedDrawables.getEntityById(imageId);
        if (ret != null)
            return ret;
        ret = connector.loadImage(imageId);
        cachedDrawables.addEntityToMap(imageId, ret);
        return ret;
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
