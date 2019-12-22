package com.example.ismobileapp;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.*;
import com.example.ismobileapp.model.Category;
import com.example.ismobileapp.model.Facility;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        ((ImageView)findViewById(R.id.detailed_facility_image)).setImageDrawable(facility.getImage());
        ((TextView) findViewById(R.id.facility_description)).setText(facility.getDescription());
        createCategoryCloud();
    }

    private void createCategoryCloud() {
        List<String> cats = Arrays.asList(
                "Sample_University",
                "Sample_Property",
                "Sample_Research Center",
                "Sample_Factory facility",
                "Sample_Startup IT"
        );
        GridLayout tagCloud = findViewById(R.id.tag_cloud);
        tagCloud.setUseDefaultMargins(true);
        List<Button> btns = cats.stream().map(
                x -> createButton(x, tagCloud.getContext())).collect(Collectors.toList()
        );
        for (Button b : btns) {
            tagCloud.addView(b);
        }
    }

    public static Button createButton(String title, Context ctx) {
        Button btn = new Button(ctx, null, 0, R.style.tag_cloud_button);
        btn.setText(title);
        return btn;
    }
}
