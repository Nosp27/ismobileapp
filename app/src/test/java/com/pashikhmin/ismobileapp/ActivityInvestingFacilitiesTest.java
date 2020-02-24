package com.pashikhmin.ismobileapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import com.pashikhmin.ismobileapp.R;
import com.pashikhmin.ismobileapp.model.Facility;
import com.pashikhmin.ismobileapp.network.Connectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowActivity;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class ActivityInvestingFacilitiesTest {
    ActivityInvestingFacilities briefView;
    FacilityDetailed detailedView;

    @Before
    public void setUp() {
        Connectors.setDebugMode(true);
        briefView = Robolectric
                .buildActivity(ActivityInvestingFacilities.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void testLikesRemain() throws IOException {
        // Wait until facilities load
        // Activity obtains lock in main thread at onCreate method,
        // so there is no race condition
        try {
            briefView.facilityLoadLock.lock();
        } finally {
            briefView.facilityLoadLock.unlock();
            assertNotNull(briefView.facilities);
        }

        // Assuming that our connector supports like changes
        for (int i = 0; i < briefView.facilities.size(); i++) {
            GridView facilityContainer = briefView.findViewById(R.id.viewFacilities);

            // locate facility list item and click on it
            View facilityView = facilityContainer.getAdapter().getView(i, null, null);
            facilityView.performClick();
            ShadowActivity shadowActivity = Shadows.shadowOf(briefView);
            ShadowActivity.IntentForResult intent =
                    shadowActivity.peekNextStartedActivityForResult();

            // assert starting facility detailed activity
            assertEquals(FacilityDetailed.class.getCanonicalName(), intent.intent.getComponent().getClassName());
        }
    }
}