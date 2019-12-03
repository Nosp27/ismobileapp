package com.example.ismobileapp.network.json;

import com.example.ismobileapp.model.Region;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * JUnit4 unit tests for the calculator logic.
 */
@RunWith(AndroidJUnit4.class)
public class JSONModellerTest {
    @Test
    public void toJSON() {
        Region region = new Region();
        region.id = 3;
        region.name = "SAS";
        String actual = JSONModeller.toJSON(region).toString();
        Assert.assertEquals("{\"name\":\"SAS\",\"id\":3}", actual);
    }

    @Test
    public void fromJSON() throws JSONException {
        String json = "{\"name\":\"SAS\",\"id\":3}";
        Region region = JSONModeller.fromJSON(Region.class, new JSONObject(json));

        Assert.assertNotNull(region);
        Assert.assertEquals("SAS", region.name);
        Assert.assertEquals(3, (int) region.id);
    }
}