package com.pashikhmin.ismobileapp;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class OneShotFragment extends Fragment {
    private static final String TAG = "OneShotFragment";

    @Override
    public void onInflate(@NonNull Context context, @NonNull AttributeSet attrs, @Nullable Bundle savedInstanceState) {
        FragmentManager fm = getFragmentManager();
        try {
            if (fm != null) {
                fm.beginTransaction().remove(this).commit();
            }
        } catch (IllegalStateException e) {
            String msg = "onInflate: Tried to remove fragment in %s (in %s), but IllegalStateException has been thrown";
            msg = String.format(msg, getClass().getCanonicalName(), getActivity().getClass().getCanonicalName());
            Log.e(TAG, msg);
        }
        super.onInflate(context, attrs, savedInstanceState);
    }
}
