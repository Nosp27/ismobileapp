package com.pashikhmin.ismobileapp;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class HeaderFragment extends OneShotFragment {
    private HeaderFragmentRequred requester;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        requester = (HeaderFragmentRequred) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(requester.resourceId(getTag()), container, false);
        String tag = getTag();
        if (tag != null)
            ((TextView) contentView.findViewById(R.id.tv_header)).setText(requester.topic(getTag()));
        ((TextView) contentView.findViewById(R.id.tv_user_name)).setText(requester.userName());
        return contentView;
    }
}
