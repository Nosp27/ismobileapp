package com.pashikhmin.ismobileapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.pashikhmin.ismobileapp.R;
import com.pashikhmin.ismobileapp.model.Category;
import com.pashikhmin.ismobileapp.model.Facility;
import androidx.appcompat.app.AppCompatActivity;
import com.pashikhmin.ismobileapp.network.Connectors;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTaskResult;
import com.pashikhmin.ismobileapp.network.loadTask.SubmitLikesTask;

import java.io.IOException;

public class FacilityDetailed extends AppCompatActivity {
    private Facility facility;
    private Intent resultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_detailed);

        facility = (Facility) getIntent().getSerializableExtra(ActivityInvestingFacilities.FACILITY_TAG);
        if (facility == null)
            return;
        initResult();
        ((TextView) findViewById(R.id.facility_name)).setText(facility.getName());
        ((ImageView) findViewById(R.id.detailed_facility_image)).setImageDrawable(facility.getImage());
        ((TextView) findViewById(R.id.facility_description)).setText(facility.getDescription());
        ImageButton likeButton = findViewById(R.id.liked);
        setLikeState(likeButton);
        likeButton.setOnClickListener(e -> likeFacility(facility, e));
        createCategoryCloud();
    }

    private void initResult() {
        resultIntent = new Intent();
        setResult(0, resultIntent);
    }

    private void likeFacility(Facility f, View e) {
        new SubmitLikesTask(
                (res) -> onChangedLike(res, e)
        ).execute(f);
    }

    private void onChangedLike(LoadTaskResult<Boolean> res, View e) {
        if (!res.successful()) {
            return;
        }

        setLikeState((ImageButton) e);
    }

    private void setLikeState(ImageButton likeButton) {
        boolean liked = facility.getLiked();
        likeButton.setImageResource(
                liked ?
                        android.R.drawable.star_big_on :
                        android.R.drawable.star_big_off
        );

        resultIntent.putExtra("liked", liked);
        setResult(0, resultIntent);
    }

    private void createCategoryCloud() {
        if (facility.getCategories() == null)
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
