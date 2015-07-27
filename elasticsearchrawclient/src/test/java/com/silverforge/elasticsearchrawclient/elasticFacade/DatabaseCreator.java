package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.util.Log;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.ElasticClientApp;
import com.silverforge.elasticsearchrawclient.R;
import com.silverforge.elasticsearchrawclient.connector.ConnectorSettings;
import com.silverforge.elasticsearchrawclient.testModel.City;
import com.silverforge.elasticsearchrawclient.utils.StreamUtils;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.fail;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Ignore
public final class DatabaseCreator {
    private static final String TAG = DatabaseCreator.class.getName();
    private ElasticClient client;

    public DatabaseCreator() {
        String elasticUrl = "https://silverforge.east-us.azr.facetflow.io";
        String elasticApiKey = "ZjjnkNMgh0uj5yCFIvYVGQsueESCLj1k";
        ConnectorSettings settings = ConnectorSettings
                .builder()
                .baseUrl(elasticUrl)
                .userName(elasticApiKey)
                .build();

        try {
            client = new ElasticClient(settings);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void prepare() {
        try {
            InputStream createIndexCities
                = ElasticClientApp
                    .getAppContext()
                    .getResources()
                    .openRawResource(R.raw.create_index_cities);

            InputStream createIndexTestCities
                = ElasticClientApp
                    .getAppContext()
                    .getResources()
                    .openRawResource(R.raw.create_index_testcities);

            String createCityData = StreamUtils.convertStreamToString(createIndexCities);
            String createTestCityData = StreamUtils.convertStreamToString(createIndexTestCities);

            client.removeIndices(new String[] {"cities", "testcities"});

            client.createIndex("cities", createCityData);
            client.createIndex("testcities", createTestCityData);

            client.addDocument(new City("Karcag"), "cities", "city", "karcag");
            client.addDocument(new City("Budapest"), "cities", "city", "budapest");

            client.addDocument(new City("customCityForTesting"), "testcities", "testcity", "customCity");
        } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }
}
