package com.pashikhmin.ismobileapp.cache;

import android.util.Pair;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.pashikhmin.ismobileapp.model.Category;
import com.pashikhmin.ismobileapp.model.Criteries;
import com.pashikhmin.ismobileapp.model.Facility;
import com.pashikhmin.ismobileapp.model.Region;
import com.pashikhmin.ismobileapp.network.StubConnector;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class CacheTest {
    @Test
    public void testCachingPipeline() throws IOException {
        StubResourceSupplier stubConnector = new StubResourceSupplier();
        Cache cache = new Cache(stubConnector);

        List<Pair<Supplier<?>, Supplier<?>>> parametrize = Arrays.asList(
                new Pair<>(() -> cache.cachedRegions, cache::getAllRegions),
                new Pair<>(() -> cache.cachedCategories, cache::getAllCategories),
                new Pair<>(() -> cache.cachedDrawables, () -> cache.getBinaryDataProvider().loadImage(0))
        );

        for (Pair<Supplier<?>, Supplier<?>> parameters : parametrize) {
            Assert.assertNull(parameters.first.get());
        }

        for (Pair<Supplier<?>, Supplier<?>> parameters : parametrize) {
            Assert.assertNotNull(parameters.second.get());
            Assert.assertNotNull(parameters.first.get());
        }

        stubConnector.alert = true;
        for (Pair<Supplier<?>, Supplier<?>> parameters : parametrize) {
            parameters.second.get();
        }

        Assert.assertEquals(1, cache.cachedDrawables.getAllEntities().size());
    }

    private interface Supplier<T> {
        T get() throws IOException;
    }

    private static class StubResourceSupplier extends StubConnector {
        boolean alert = false;

        @Override
        public List<Region> getAllRegions() {
            if (alert)
                throw new RuntimeException("Loading alert from getAllRegions");
            return super.getAllRegions();
        }

        @Override
        public List<Category> getAllCategories() {
            if (alert)
                throw new RuntimeException("Loading alert from getAllCategories");
            return super.getAllCategories();
        }

        @Override
        public List<Facility> getCriterizedFacilities(Criteries criteries) {
            if (alert)
                throw new RuntimeException("Loading alert from getCriterizedFacilities");
            return super.getCriterizedFacilities(criteries);
        }
    }
}