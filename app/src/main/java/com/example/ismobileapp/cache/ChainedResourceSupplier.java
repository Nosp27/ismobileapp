package com.example.ismobileapp.cache;

import com.example.ismobileapp.model.Category;
import com.example.ismobileapp.model.Criteries;
import com.example.ismobileapp.model.Facility;
import com.example.ismobileapp.model.Region;
import com.example.ismobileapp.resourceSupplier.CannotServeException;
import com.example.ismobileapp.resourceSupplier.ResourceSupplier;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChainedResourceSupplier implements ResourceSupplier {
    private ChainedResourceSupplier() {
        resourceSuppliers = new ArrayList<>();
    }

    List<ResourceSupplier> resourceSuppliers;

    private <T> T chainWrap(ChainingFunction<T> innerMethod) throws IOException {
        Iterator<ResourceSupplier> supplierIterator = resourceSuppliers.iterator();
        while (supplierIterator.hasNext()) {
            try {
                return innerMethod.apply(supplierIterator.next());
            } catch (CannotServeException e) {
                if (!supplierIterator.hasNext())
                    throw e;
            }
        }
        throw new IllegalArgumentException(
                String.format("Empty supplier list: %s. Length: %d", resourceSuppliers, resourceSuppliers.size())
        );
    }

    @Override
    public List<Region> getAllRegions() throws IOException {
        return chainWrap(ResourceSupplier::getAllRegions);
    }

    @Override
    public List<Category> getAllCategories() throws IOException {
        return chainWrap(ResourceSupplier::getAllCategories);
    }

    @Override
    public List<Facility> getCriterizedFacilities(Criteries criteries) throws IOException {
        return chainWrap(supplier -> supplier.getCriterizedFacilities(criteries));
    }

    @Override
    public InputStream loadImage(Integer key) throws IOException {
        return chainWrap(x -> x.loadImage(key));
    }

    private interface ChainingFunction<R> {
        R apply(ResourceSupplier supplier) throws IOException;
    }

    public static class Builder {
        private ChainedResourceSupplier _resultChain;
        public Builder() {
            _resultChain = new ChainedResourceSupplier();
        }
        public Builder supplier(ResourceSupplier resourceSupplier) {
            _resultChain.resourceSuppliers.add(resourceSupplier);
            return this;
        }

        public ResourceSupplier build() {
            return _resultChain;
        }
    }
}
