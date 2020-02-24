package com.pashikhmin.ismobileapp;

import android.content.Intent;
import android.widget.Spinner;
import com.pashikhmin.ismobileapp.R;
import com.pashikhmin.ismobileapp.model.Category;
import com.pashikhmin.ismobileapp.model.Region;
import com.pashikhmin.ismobileapp.network.Connectors;
import com.pashikhmin.ismobileapp.resourceSupplier.ResourceSupplier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class MainActivityTest {
    private MainActivity activity;
    private ResourceSupplier resourceSupplier;

    @Before
    public void setUp() throws Exception {
        Connectors.setDebugMode(true);
        resourceSupplier = Connectors.getDefaultCachedConnector();
        activity = Robolectric.buildActivity(MainActivity.class).create().resume().get();
    }

    @Test
    public void testNoSmoke() {
        assertNotNull(activity);
    }

    @Test
    public void testRegionAndCategoryItems() throws IOException {
        Region[] expectedRegions = resourceSupplier.getAllRegions().toArray(new Region[0]);
        Category[] expectedCategories = resourceSupplier.getAllCategories().toArray(new Category[0]);
        Spinner regionsView = (activity.findViewById(R.id.listRegions));
        Spinner categoryView = (activity.findViewById(R.id.listCategories));

        Region[] actualRegions = new Region[regionsView.getCount()];
        for(int i = 0; i < actualRegions.length; i++)
            actualRegions[i] = (Region)regionsView.getAdapter().getItem(i);
        assertArrayEquals(expectedRegions, actualRegions);
        
        Category[] actualCategories = new Category[categoryView.getCount()];
        for(int i = 0; i < actualCategories.length; i++)
            actualCategories[i] = ((Category) categoryView.getAdapter().getItem(i));
        assertArrayEquals(expectedCategories, actualCategories);
    }

    @Test
    public void testFacilityLoadOnSelectButtonClick() {
        activity.findViewById(R.id.btn_select_regions).performClick();
        Intent intent = Shadows.shadowOf(activity).peekNextStartedActivity();
        assertEquals(ActivityInvestingFacilities.class.getCanonicalName(), intent.getComponent().getClassName());
    }
}