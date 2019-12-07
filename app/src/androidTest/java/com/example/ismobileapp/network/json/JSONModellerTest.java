package com.example.ismobileapp.network.json;

import com.example.ismobileapp.model.Criteries;
import com.example.ismobileapp.model.Region;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * JUnit4 unit tests for the calculator logic.
 */
@RunWith(AndroidJUnit4.class)
public class JSONModellerTest {
    @Test
    public void toJSON() {
        Region region = new Region();
        region.regionId = 3;
        region.regionName = "SAS";
        String actual = JSONModeller.toJSON(region).toString();
        Assert.assertEquals("{\"name\":\"SAS\",\"id\":3}", actual);
    }

    @Test
    public void fromJSON() throws JSONException {
        String json = "{\"name\":\"SAS\",\"id\":3}";
        Region region = JSONModeller.fromJSON(Region.class, new JSONObject(json));

        Assert.assertNotNull(region);
        Assert.assertEquals("SAS", region.regionName);
        Assert.assertEquals(3, (int) region.regionId);
    }

    @Test
    public void testMatchJson() {
        Criteries crit = new Criteries();
        crit.categories = new String[]{"c1", "c2"};
        crit.regions = new Integer[]{1,2};

        class ApiCriteres implements Serializable {
            public List<String> categories = Arrays.asList("c1", "c2");
            public List<Integer> regions = Arrays.asList(1,2);
        }

        Assert.assertEquals(JSONModeller.toJSON(new ApiCriteres()).toString(), JSONModeller.toJSON(crit).toString());
    }
}