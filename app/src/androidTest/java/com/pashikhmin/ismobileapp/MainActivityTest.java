package com.pashikhmin.ismobileapp;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import com.pashikhmin.ismobileapp.model.Category;
import com.pashikhmin.ismobileapp.model.Region;
import com.pashikhmin.ismobileapp.network.Connectors;
import com.pashikhmin.ismobileapp.resourceSupplier.ResourceSupplier;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4ClassRunner.class)
public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenario =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testRegionAndCategoryItems() throws IOException {
        ResourceSupplier resourceSupplier = Connectors.getDefaultCachedConnector();
        Region[] expectedRegions = resourceSupplier.getAllRegions().toArray(new Region[0]);
        Category[] expectedCategories = resourceSupplier.getAllCategories().toArray(new Category[0]);

        for (Region region : expectedRegions) {
            onData(is(region))
                    .inAdapterView(withId(R.id.listRegions))
                    .check(matches(not(doesNotExist())));
        }

        for (Category category : expectedCategories) {
            onData(is(category))
                    .inAdapterView(withId(R.id.listCategories))
                    .check(matches(not(doesNotExist())));
        }
    }
}