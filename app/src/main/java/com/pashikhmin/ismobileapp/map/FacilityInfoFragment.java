package com.pashikhmin.ismobileapp.map;

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
import com.example.ismobileapp.R;
import com.pashikhmin.ismobileapp.model.Facility;

public class FacilityInfoFragment extends View {
    Facility associatedFacility;

    public FacilityInfoFragment(Context context, Facility associatedFacility) {
        super(context);
        this.associatedFacility = associatedFacility;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View helperFragment = inflater.inflate(R.layout.layout_facility_brief, container, false);
        if (associatedFacility != null) {
            ((TextView) helperFragment.findViewById(R.id.list_title)).setText("AAAA");
            ((TextView) helperFragment.findViewById(R.id.facility_region)).setText("SADASD");
        }
        return helperFragment;
    }
}
