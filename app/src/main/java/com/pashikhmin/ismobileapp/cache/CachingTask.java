package com.pashikhmin.ismobileapp.cache;

import android.util.Log;
import com.pashikhmin.ismobileapp.resourceSupplier.ResourceSupplier;

import java.io.IOException;
import java.util.concurrent.Executors;

public class CachingTask {
    private static final String TAG = "CachingTask";

    private Cache supplier;
    private static final int timeout = 10000;

    public CachingTask(ResourceSupplier parentSupplier) {
        try {
            supplier = new Cache(parentSupplier);
            Executors.newSingleThreadExecutor().submit(this::asyncProcess);
        } catch (IOException e) {
            Log.e(TAG, "Could not initialize resource supplier", e);
        }
    }

    private void asyncProcess() {
        try {
            while (true) {
                synchronized (supplier) {
                    supplier.wait(timeout);
                }
                supplier.resetCaches();
            }
        } catch (InterruptedException ignore) {
        }
    }

    public ResourceSupplier getResourceSupplier() {
        return supplier;
    }
}
