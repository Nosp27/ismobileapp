package com.pashikhmin.ismobileapp.network.json;

import com.pashikhmin.ismobileapp.model.Criteries;
import com.pashikhmin.ismobileapp.model.Facility;
import com.pashikhmin.ismobileapp.model.Region;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.pashikhmin.ismobileapp.network.json.JSONField;
import com.pashikhmin.ismobileapp.network.json.JSONModeller;
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
    public void toJSONRegion() {
        Region region = new Region();
        region.setRegionId(3);
        region.setRegionName("SAS");
        String actual = JSONModeller.toJSON(region).toString();
        Assert.assertEquals("{\"imageId\":null,\"regionId\":3,\"regionName\":\"SAS\"}", actual);
    }

    @Test
    public void fromJSONRegion() throws JSONException {
        String json = "{\"regionName\":\"SAS\",\"regionId\":3, \"imageId\": null}";
        Region region = JSONModeller.fromJSON(Region.class, new JSONObject(json));

        Assert.assertNotNull(region);
        Assert.assertEquals("SAS", region.getTitle());
        Assert.assertEquals(3, (int) region.getRegionId());
    }

    @Test
    public void fromJSONCategory() throws JSONException {
        String json =
                "    {\n" +
                        "        \"_id\": 1,\n" +
                        "        \"name\": \"HSE\",\n" +
                        "        \"description\": \"HSE Shabla\",\n" +
                        "        \"lat\": 55.754096,\n" +
                        "        \"lng\": 37.649238,\n" +
                        "        \"imageId\": 0,\n" +
                        "        \"categories\": [\n" +
                        "            {\n" +
                        "                \"catName\": \"Research\",\n" +
                        "                \"imageId\": null\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"region\": {\n" +
                        "            \"regionId\": 0,\n" +
                        "            \"regionName\": \"Moscow\",\n" +
                        "            \"imageId\": null\n" +
                        "        }\n" +
                        "    }\n";
        Facility facility = JSONModeller.fromJSON(Facility.class, new JSONObject(json));
        Assert.assertNotNull(facility.getRegion());
        Assert.assertNotNull(facility.getCategories());
    }

    @Test
    public void testMatchJsonArrayAndList() {
        Criteries crit = new Criteries();
        crit.categories = new Integer[]{0, 1};
        crit.regions = new Integer[]{1, 2};

        class ApiCriteres implements Serializable {
            @JSONField
            public List<Integer> categories = Arrays.asList(0, 1);
            @JSONField
            public List<Integer> regions = Arrays.asList(1, 2);
        }

        Assert.assertEquals(JSONModeller.toJSON(new ApiCriteres()).toString(), JSONModeller.toJSON(crit).toString());
    }
}