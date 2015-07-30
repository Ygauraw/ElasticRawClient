package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.util.Log;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.connector.ConnectorSettings;
import com.silverforge.elasticsearchrawclient.testModel.City;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ElasticClientSearchTest extends ElasticClientBaseTest {
    private static final String TAG = ElasticClientSearchTest.class.getName();

    // region Happy path

    @Test
    public void searchWithIndexWithTypeTest() {
        List<City> cities = client.search("{\"query\":{\"match_all\": {}}}", City.class);
        assertNotNull(cities);
    }

    @Test
    public void searchWithoutIndexWithoutTypeTest() {
        ConnectorSettings settings = ConnectorSettings
                .builder()
                .baseUrl(ELASTIC_URL)
                .userName(ELASTIC_APIKEY)
                .build();

        try {
            ElasticClient testClient = new ElasticClient(settings);
            List<City> cities = testClient.search("{\"query\":{\"match_all\": {}}}", City.class);

            assertNotNull(cities);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void searchWithoutIndexWithTypeTest() {
        ConnectorSettings settings = ConnectorSettings
                .builder()
                .baseUrl(ELASTIC_URL)
                .types(new String[]{"city", "testcity"})
                .userName(ELASTIC_APIKEY)
                .build();

        try {
            ElasticClient testClient = new ElasticClient(settings);
            List<City> cities = testClient.search("{\"query\":{\"match_all\": {}}}", City.class);

            assertNotNull(cities);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void searchWithIndexWithoutTypeTest() {
        ConnectorSettings settings = ConnectorSettings
                .builder()
                .baseUrl(ELASTIC_URL)
                .indices(new String[]{"cities", "testcities"})
                .userName(ELASTIC_APIKEY)
                .build();

        try {
            ElasticClient testClient = new ElasticClient(settings);
            List<City> cities = testClient.search("{\"query\":{\"match_all\": {}}}", City.class);

            assertNotNull(cities);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    // endregion

    // region Sad path

    @Test
    public void searchEmptyQueryTest() {
        List<City> cities = client.search("{}", City.class);

        assertNotNull(cities);
    }

    @Test
    public void searchNullTest() {
        List<City> cities = client.search(null, City.class);

        assertNotNull(cities);
    }

    @Test
    public void searchEmptyTest() {
        List<City> cities = client.search("", City.class);

        assertNotNull(cities);
    }

    // endregion
}
