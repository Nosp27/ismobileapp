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

public class FacilityDetailed extends AppCompatActivity implements HeaderFragmentRequred {
    private Facility facility;
    private Intent resultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facility = (Facility) getIntent().getSerializableExtra(ActivityInvestingFacilities.FACILITY_TAG);
        if (facility == null)
            return;
        setContentView(R.layout.activity_facility_detailed);
        initResult();
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
                        R.drawable.ic_star_filled :
                        R.drawable.ic_star_empty
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

    @Override
    public int resourceId(String tag) {
        return R.layout.header_fragment;
    }

    @Override
    public String topic(String tag) {
        return facility.getName();
    }
}
