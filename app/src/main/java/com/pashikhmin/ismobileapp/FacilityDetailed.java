package com.pashikhmin.ismobileapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.*;
import com.pashikhmin.ismobileapp.model.Category;
import com.pashikhmin.ismobileapp.model.Facility;
import androidx.appcompat.app.AppCompatActivity;
import com.pashikhmin.ismobileapp.network.loadTask.LoadTaskResult;
import com.pashikhmin.ismobileapp.network.loadTask.SubmitLikesTask;
import org.w3c.dom.Text;

import java.util.*;

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
        renderFacilityProperties();
        ImageButton likeButton = findViewById(R.id.liked);
        setLikeState(likeButton);
        likeButton.setOnClickListener(e -> likeFacility(facility, e));
        createCategoryCloud();
    }

    private void initResult() {
        resultIntent = new Intent();
        setResult(0, resultIntent);
    }

    private void renderFacilityProperties() {
        ((ImageView) findViewById(R.id.detailed_facility_image)).setImageDrawable(facility.getImage());
        ((TextView) findViewById(R.id.facility_description)).setText(facility.getDescription());
        LinearLayout propertyContainer = findViewById(R.id.property_layout);
        if(propertyContainer.getChildCount() > 0)
            propertyContainer.removeAllViews();
        for (Pair<String, String> prop : facilityProperties(facility)) {
            View propertyToAdd = getLayoutInflater()
                    .inflate(R.layout.facility_detailed_property, propertyContainer, false);
            propertyContainer.addView(propertyToAdd);
            ((TextView) propertyToAdd.findViewById(R.id.property_header)).setText(prop.first);
            ((TextView) propertyToAdd.findViewById(R.id.property_value)).setText(prop.second);
        }
    }

    private List<Pair<String, String>> facilityProperties(Facility f) {
        List<Pair<String, String>> ret = new LinkedList<>();
        ret.add(new Pair<>(strById(R.string.region), coalesce(f.getRegionTitle())));
        ret.add(new Pair<>(strById(R.string.utility), coalesce(f.getUtility())));
        ret.add(new Pair<>(strById(R.string.employees), coalesce(f.getEmployees())));
        ret.add(new Pair<>(strById(R.string.region), "$ " + coalesce(f.getInvestmentSize())));
        ret.add(new Pair<>(strById(R.string.profitability), coalesce(f.getProfitability()) + " %"));
        return ret;
    }

    private String strById(int id) {
        return getResources().getString(id);
    }

    private String coalesce (Object value) {
        return value == null || value.toString().isEmpty() ? strById(R.string.undefined) : value.toString();
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
        Boolean liked = facility.getLiked();
        if (liked == null) {
            likeButton.setVisibility(View.INVISIBLE);
        } else {
            likeButton.setImageResource(
                    liked ?
                            R.drawable.ic_star_filled :
                            R.drawable.ic_star_empty
            );
            resultIntent.putExtra("liked", liked);
        }
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
