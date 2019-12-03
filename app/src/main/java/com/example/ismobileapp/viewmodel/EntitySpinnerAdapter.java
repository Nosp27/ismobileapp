package com.example.ismobileapp.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.ismobileapp.R;
import com.example.ismobileapp.model.Entity;
import com.example.ismobileapp.model.callbacks.EntityListener;

import java.util.Iterator;

public class EntitySpinnerAdapter extends BaseEntityAdapter {
    public EntitySpinnerAdapter(Context context, int resource, EntityListener listener) {
        super(context, resource, listener);
    }

    public EntitySpinnerAdapter(Context context, EntityListener listener) {
        this(context, R.layout.list_item_criteria, listener);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getViewBase(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int item_resource = android.R.layout.simple_spinner_item;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(item_resource, parent, false);
        }
        ((TextView) convertView).setText(getSelectedEntitiesString());
        return convertView;
    }

    private String getSelectedEntitiesString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Entity> entityIterator = entityListener.getSelectedEntities().iterator();
        while (entityIterator.hasNext()) {
            sb.append(entityIterator.next().getTitle());
            if (entityIterator.hasNext())
                sb.append(", ");
        }

        String ret = sb.toString();

        if (ret.isEmpty())
            ret = "Nothing selected";

        return ret;
    }
}
