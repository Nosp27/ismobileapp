package com.pashikhmin.ismobileapp.viewmodel;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.pashikhmin.ismobileapp.R;
import com.pashikhmin.ismobileapp.model.Entity;
import com.pashikhmin.ismobileapp.model.Facility;
import com.pashikhmin.ismobileapp.model.callbacks.EntityListener;

import java.util.List;

public abstract class BaseEntityAdapter extends ArrayAdapter<Entity> {
    protected int resource;
    protected List<Entity> entities;
    protected EntityListener<Entity> entityListener;

    public BaseEntityAdapter(Context context, int resource, EntityListener<Entity> listener) {
        this(context, resource, listener, listener.getEntities());
    }

    private BaseEntityAdapter(Context context, int resource, EntityListener<Entity> listener, List<Entity> content) {
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
        if (entity.getImage() != null)
            ((ImageView) convertView.findViewById(R.id.item_img)).setImageDrawable(entity.getImage());

        Drawable img = entity.getImage();
        if (img != null)
            ((ImageView) convertView.findViewById(R.id.item_img)).setImageDrawable(img);

        if (entity instanceof Facility) {
            int newResource =
                    ((Facility) entity).getLiked() ?
                            android.R.drawable.star_big_on :
                            android.R.drawable.star_big_off;
            ((ImageButton) convertView.findViewById(R.id.liked)).setImageResource(newResource);
        }

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
