package com.pashikhmin.ismobileapp;

import androidx.test.espresso.action.AdapterViewProtocol;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import com.pashikhmin.ismobileapp.model.Category;
import com.pashikhmin.ismobileapp.model.Entity;
import com.pashikhmin.ismobileapp.model.Region;
import com.pashikhmin.ismobileapp.network.connectors.Connectors;
import com.pashikhmin.ismobileapp.resourceSupplier.ApiConnector;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.*;

@RunWith(AndroidJUnit4ClassRunner.class)
public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenario =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testRegionAndCategoryItems() throws IOException {
        ApiConnector apiConnector = Connectors.api();
        Region[] expectedRegions = apiConnector.getAllRegions().toArray(new Region[0]);
        Category[] expectedCategories = apiConnector.getAllCategories().toArray(new Category[0]);

        onView(withId(R.id.listRegions)).check(matches(isDisplayed()));

        for (Region region : expectedRegions) {
            onData(is(regionSimilarTo(region)))
                    .inAdapterView(withId(R.id.listRegions))
                    .check(matches(not(doesNotExist())));
        }

        for (Category category : expectedCategories) {
            onData(is(regionSimilarTo(category)))
                    .inAdapterView(withId(R.id.listCategories))
                    .check(matches(not(doesNotExist())));
        }
    }

    private Matcher<? extends Entity> regionSimilarTo(Entity reference) {
        return new TypeSafeMatcher<Entity>() {
            @Override
            protected boolean matchesSafely(Entity item) {
                return item.getId().equals(reference.getId()) && item.getTitle().equals(reference.getTitle());
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }
}