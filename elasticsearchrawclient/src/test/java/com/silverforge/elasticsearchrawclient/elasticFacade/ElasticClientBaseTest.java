package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.util.Log;

import com.silverforge.elasticsearchrawclient.connector.ConnectorSettings;

import org.junit.Before;

import java.net.URISyntaxException;

import static org.junit.Assert.fail;

public class ElasticClientBaseTest {
    private static final String TAG = ElasticClientSearchTest.class.getName();
    protected static final String ELASTIC_URL = "https://silverforge.east-us.azr.facetflow.io";
    protected static final String ELASTIC_APIKEY = "ZjjnkNMgh0uj5yCFIvYVGQsueESCLj1k";
    protected static final String[] ELASTIC_INDICES = new String[] {"cities"};
    protected ElasticClient client;

    public ElasticClientBaseTest() {
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
}
