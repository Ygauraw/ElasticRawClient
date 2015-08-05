package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.util.Log;

import com.silverforge.elasticsearchrawclient.connector.ConnectorSettings;

import java.net.URISyntaxException;

import static org.junit.Assert.fail;

public class ElasticClientBaseTest {
    private static final String TAG = ElasticClientSearchTest.class.getName();
    protected static final String ELASTIC_URL = "https://mgj.east-us.azr.facetflow.io";
    protected static final String ELASTIC_APIKEY = "wihIilbbekmCeppKlgQXDwpSZEUekkk0";
    protected static final String[] ELASTIC_INDICES = new String[] {"cities"};
    protected static final String[] ELASTIC_TYPES = new String[] {"city"};
    protected ElasticRawClient client;

    public ElasticClientBaseTest() {
        ConnectorSettings settings = ConnectorSettings
                .builder()
                .baseUrl(ELASTIC_URL)
                .indices(ELASTIC_INDICES)
                .types(ELASTIC_TYPES)
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
