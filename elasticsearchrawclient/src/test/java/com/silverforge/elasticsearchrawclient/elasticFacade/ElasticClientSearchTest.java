package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.util.Log;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.connector.ConnectorSettings;
import com.silverforge.elasticsearchrawclient.exceptions.ServerIsNotAvailableException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ElasticClientSearchTest extends ElasticClientBaseTest {
    private static final String TAG = ElasticClientSearchTest.class.getName();

    // region Happy path

    @Test
    public void searchWithIndexWithTypeTest() {
        try {
            String searchResult = client.search("{\"query\":{\"match_all\": {}}}");

            assertNotNull(searchResult);
        } catch (NoSuchAlgorithmException | IOException | KeyManagementException | ServerIsNotAvailableException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
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
            String searchResult = testClient.search("{\"query\":{\"match_all\": {}}}");

            assertNotNull(searchResult);
        } catch (URISyntaxException | KeyManagementException | NoSuchAlgorithmException | IOException | ServerIsNotAvailableException e) {
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
            String searchResult = testClient.search("{\"query\":{\"match_all\": {}}}");

            assertNotNull(searchResult);
        } catch (URISyntaxException | KeyManagementException | NoSuchAlgorithmException | IOException | ServerIsNotAvailableException e) {
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
            String searchResult = testClient.search("{\"query\":{\"match_all\": {}}}");

            assertNotNull(searchResult);
        } catch (URISyntaxException | KeyManagementException | NoSuchAlgorithmException | IOException | ServerIsNotAvailableException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    // endregion

    // region Sad path

    @Test
    public void searchEmptyQueryTest() {
        try {
            String search = client.search("{}");

            assertNotNull(search);
        } catch (NoSuchAlgorithmException | IOException | KeyManagementException | ServerIsNotAvailableException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void searchNullTest() {
        try {
            String search = client.search(null);

            assertNotNull(search);
        } catch (NoSuchAlgorithmException | IOException | KeyManagementException | ServerIsNotAvailableException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void searchEmptyTest() {
        try {
            String search = client.search("");

            assertNotNull(search);
        } catch (NoSuchAlgorithmException | IOException | KeyManagementException | ServerIsNotAvailableException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    // endregion
}
