package com.pashikhmin.ismobileapp.model.callbacks;

import com.pashikhmin.ismobileapp.model.Entity;

import java.util.List;

public interface EntityListener<T extends Entity> {
    List<T> getEntities();
    List<T> getSelectedEntities();

    void selectEntity(T entity);
    void deselectEntity(T entity);
}
