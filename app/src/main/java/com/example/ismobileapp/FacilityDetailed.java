package com.example.ismobileapp;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.*;
import com.example.ismobileapp.model.Category;
import com.example.ismobileapp.model.Facility;
import androidx.appcompat.app.AppCompatActivity;

public class FacilityDetailed extends AppCompatActivity {
    private Facility facility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_detailed);

        facility = (Facility) getIntent().getSerializableExtra(ActivityInvestingFacilities.FACILITY_TAG);
        if (facility == null)
            return;
        ((TextView) findViewById(R.id.facility_name)).setText(facility.getName());
        ((ImageView) findViewById(R.id.detailed_facility_image)).setImageDrawable(facility.getImage());
        ((TextView) findViewById(R.id.facility_description)).setText(facility.getDescription());
        createCategoryCloud();
    }

    private void createCategoryCloud() {
        if(facility.getCategories() == null)
            return;

        GridLayout tagCloud = findViewById(R.id.tag_cloud);
        tagCloud.setUseDefaultMargins(true);

        for (Category cat : facility.getCategories()) {
            Button btn = createButton(cat.getTitle(), tagCloud.getContext());
            tagCloud.addView(btn);
        }
    }

    public static Button createButton(String title, Context ctx) {
        Button btn = new Button(ctx, null, 0, R.style.tag_cloud_button);
        btn.setText(title);
        return btn;
    }
}
