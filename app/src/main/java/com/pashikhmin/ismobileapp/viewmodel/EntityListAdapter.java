package com.pashikhmin.ismobileapp.viewmodel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.pashikhmin.ismobileapp.model.callbacks.EntityListener;

public class EntityListAdapter extends BaseEntityAdapter {
    public EntityListAdapter(Context context, int resource, EntityListener listener) {
        super(context, resource, listener);
        entityListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getViewBase(position, convertView, parent);
    }
}
