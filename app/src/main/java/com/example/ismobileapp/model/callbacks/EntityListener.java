package com.example.ismobileapp.model.callbacks;

import com.example.ismobileapp.model.Entity;
import com.example.ismobileapp.model.Region;

import java.util.List;

public interface EntityListener {
    List<Entity> getEntities();
    List<Entity> getSelectedEntities();

    void selectEntity(Entity entity);
    void deselectEntity(Entity entity);
}
