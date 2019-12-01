package com.example.ismobileapp;

import android.os.Bundle;
import android.widget.TextView;
import com.example.ismobileapp.model.Facility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

public class FacilityDetailed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_detailed);

        Facility facility = (Facility) getIntent().getSerializableExtra(ActivityInvestingFacilities.FACILITY_TAG);
        if (facility == null)
            return;
        ((TextView) findViewById(R.id.facility_name)).setText(facility.getName());
    }
}
