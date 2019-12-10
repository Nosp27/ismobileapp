package com.example.ismobileapp;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TextView;
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
        createCategoryCloud();
    }

    private void createCategoryCloud() {
        List<Category> cats = Arrays.asList(
                new Category("Sample_University"),
                new Category("Sample_Property"),
                new Category("Sample_Research Center"),
                new Category("Sample_Factory facility"),
                new Category("Sample_Startup IT")
        );
        GridLayout tagCloud = findViewById(R.id.tag_cloud);
        tagCloud.setUseDefaultMargins(true);
        List<Button> btns = cats.stream().map(
                x -> createButton(x.getTitle(), tagCloud.getContext())).collect(Collectors.toList()
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
