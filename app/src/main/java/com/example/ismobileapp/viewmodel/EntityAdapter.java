package com.example.ismobileapp.viewmodel;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ismobileapp.R;
import com.example.ismobileapp.model.Entity;
import com.example.ismobileapp.model.callbacks.EntityListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EntityAdapter extends ArrayAdapter<Entity> {
    // layout index
    private int resource;

    // list items
    private List<Entity> entities;

    private EntityListener entityListener;

    public EntityAdapter(Context context, int resource, EntityListener listener) {
        this(context, resource, listener.getEntities());
        entityListener = listener;
    }

    public EntityAdapter(Context context, EntityListener listener) {
        this(context, R.layout.layout_list_regions, listener.getEntities());
        entityListener = listener;
    }

    private EntityAdapter(Context context, int resource, List<Entity> entities) {
        super(context, resource, entities);
        this.resource = resource;
        this.entities = entities;
    }

    @Override
    @NotNull
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);

        Entity entity = entities.get(position);

        ((TextView) convertView.findViewById(R.id.list_title)).setText(entity.getTitle());
        ((TextView) convertView.findViewById(R.id.list_subtitle)).setText(entity.getSubtitle());

        Drawable img = entity.getImage();
        if (img != null)
            ((ImageView) convertView.findViewById(R.id.item_img)).setImageDrawable(img);

        CheckBox cb_select_item = convertView.findViewById(R.id.region_selected);
        if (cb_select_item != null) {
            cb_select_item.setFocusable(false);

            convertView.setOnClickListener((x) -> {
                boolean is_checked = !cb_select_item.isChecked();
                cb_select_item.setChecked(is_checked);
                if (entityListener != null)
                    if (is_checked)
                        entityListener.selectEntity(entity);
                    else entityListener.deselectEntity(entity);
            });
        } else
            convertView.setOnClickListener((x) -> entityListener.selectEntity(entity));
        return convertView;
    }
}
