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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.ismobileapp.R;
import com.example.ismobileapp.model.Entity;
import com.example.ismobileapp.model.callbacks.EntityListener;

import java.util.List;

public abstract class BaseEntityAdapter extends ArrayAdapter<Entity> {
    protected int resource;
    protected List<Entity> entities;
    protected EntityListener entityListener;

    public BaseEntityAdapter(Context context, int resource, EntityListener listener) {
        this(context, resource, listener, listener.getEntities());
    }

    private BaseEntityAdapter(Context context, int resource, EntityListener listener, List<Entity> content) {
        super(context, resource, content);
        this.resource = resource;
        this.entityListener = listener;
        this.entities = content;
    }

    protected View getViewBase(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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
                BaseEntityAdapter.this.notifyDataSetChanged();
            });
        } else
            convertView.setOnClickListener((x) -> entityListener.selectEntity(entity));
        return convertView;
    }
}