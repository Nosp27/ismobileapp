package com.pashikhmin.ismobileapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import com.pashikhmin.ismobileapp.model.Criteries;
import com.pashikhmin.ismobileapp.model.Facility;
import com.pashikhmin.ismobileapp.network.Connectors;
import com.pashikhmin.ismobileapp.network.StubConnector;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

@RunWith(AndroidJUnit4ClassRunner.class)
public class ActivityInvestingFacilitiesTest {
    @Rule
    public ActivityScenarioRule<ActivityInvestingFacilities> activityScenario =
            new ActivityScenarioRule<>(ActivityInvestingFacilities.class);

    @Rule
    public IntentsTestRule<ActivityInvestingFacilities> intentsTestRule =
            new IntentsTestRule<>(ActivityInvestingFacilities.class);

    private static MockFacility mockFacility;
    private int sampleFacilityIndex = 0; // index of facility under test in the list


    /**
     * Method initializes testing data
     * 1. Set up Mocked facility: this facility detects whether `setLike()` method has been called
     * 2. Set up custom connector: it act like normal stub connector,
     *  but returns only mock facility when called to return `categorizedFacilities`
     */
    @BeforeClass
    public static void setUp() {
        String facilityName = "Some mocked facility name";
        mockFacility = new MockFacility(facilityName, new Double[]{1.0, 1.0});
        Connectors.setCustomConnector(new StubConnector() {
            @Override
            public List<Facility> getCriterizedFacilities(Criteries criteries) {
                return Collections.singletonList(mockFacility);
            }
        });
    }

    @Test
    public void testFacilityListLoading() throws IOException {
        // Load and cache facilities
        List<Facility> facilities = Connectors
                .getDefaultCachedConnector()
                .getCriterizedFacilities(null);

        // Check that there is matching `list_item` for `facility`
        // and when clicked it intends to load `FacilityDetailed` activity
        // Assert that there is clickable item in list view containing facility name
        onData(instanceOf(Facility.class))
                .inAdapterView(withId(R.id.viewFacilities))
                .atPosition(sampleFacilityIndex)
                .check(matches(isClickable()))
                // Click on list item
                .perform(click());

        // Assert that when clicked, activity invokes detailed facility view and passes facility as an intent extra
        Intents.intended(
                allOf(
                        IntentMatchers.hasComponent(FacilityDetailed.class.getCanonicalName()),
                        IntentMatchers.hasExtra(
                                ActivityInvestingFacilities.FACILITY_TAG, facilities.get(sampleFacilityIndex)
                        )
                )
        );
    }

    /**
     * Test checks that facility `liked` state changes
     * if facility was marked as favorite in `FacilityDetailed` activity
     */
    @Test
    public void testFavoriteSignChanges() throws IOException {
        //Create response intent
        Intent responseIntent = new Intent();
        responseIntent.putExtra("liked", true);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, responseIntent);
        Intents.intending(IntentMatchers.hasComponent(FacilityDetailed.class.getCanonicalName())).respondWith(result);

        onData(instanceOf(Facility.class))
                .inAdapterView(withId(R.id.viewFacilities))
                .atPosition(sampleFacilityIndex)
                .check(matches(ViewMatchers.withChild(
                        withId(R.id.liked)
                )))
                .perform(click());
        Assert.assertTrue(mockFacility.setLikedCalled);
    }

    public static class MockFacility extends Facility implements Serializable {
        public boolean setLikedCalled = false;

        public MockFacility(String name, @NotNull Double[] coords) {
            super(name, coords);
        }

        @Override
        public synchronized void setLiked(Boolean liked) {
            super.setLiked(liked);
            setLikedCalled = true;
        }
    }
}