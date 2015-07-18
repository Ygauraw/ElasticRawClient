package com.silverforge.elasticsearchrawclient.ElasticFacade;

import android.util.Log;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.Connector.ConnectorSettings;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ElasticClientSearchTest {
    private static final String TAG = ElasticClientSearchTest.class.getName();
    private static final String ELASTIC_URL = "https://silverforge.east-us.azr.facetflow.io";
    private static final String ELASTIC_APIKEY = "ZjjnkNMgh0uj5yCFIvYVGQsueESCLj1k";
    private static final String[] ELASTIC_INDICES = new String[] {"cities"};
    private static final String QUERY_SEARCH_ALL = "{\"query\":{\"match_all\": {}}}";
    private static final String QUERY_EMPTY = "{}";

    private ElasticClient client;

    @Before
    public void setUp() {
        ConnectorSettings settings = ConnectorSettings
                .builder()
                .baseUrl(ELASTIC_URL)
                .indices(ELASTIC_INDICES)
                .userName(ELASTIC_APIKEY)
                .build();

        try {
            client = new ElasticClient(settings);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    // region Happy path

    @Test
    public void searchTest() {
        try {
            String search = client.search(QUERY_SEARCH_ALL);

            assertNotNull(search);
        } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    // endregion

    // region Sad path

    @Test
    public void searchEmptyTest() {
        try {
            String search = client.search(QUERY_EMPTY);

            assertNotNull(search);
        } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    // endregion
}
