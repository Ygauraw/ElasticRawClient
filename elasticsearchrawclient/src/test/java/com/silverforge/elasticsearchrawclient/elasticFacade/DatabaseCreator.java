package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.util.Log;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.ElasticClientApp;
import com.silverforge.elasticsearchrawclient.R;
import com.silverforge.elasticsearchrawclient.testModel.SimpleCity;
import com.silverforge.elasticsearchrawclient.utils.StreamUtils;
import com.silverforge.webconnector.exceptions.SettingsIsNullException;
import com.silverforge.webconnector.model.ConnectorSettings;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.net.URISyntaxException;

import static org.junit.Assert.fail;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public final class DatabaseCreator {
    private static final String TAG = DatabaseCreator.class.getName();
    private ElasticRawClient client;

    public DatabaseCreator() {
        String elasticUrl = "https://mgj.east-us.azr.facetflow.io";
        String elasticApiKey = "wihIilbbekmCeppKlgQXDwpSZEUekkk0";
        ConnectorSettings settings = ConnectorSettings
                .builder()
                .baseUrl(elasticUrl)
                .userName(elasticApiKey)
                .build();

        try {
            client = new ElasticClient(settings);
        } catch (URISyntaxException | SettingsIsNullException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    @Ignore("Execute manually once it is needed to rebuild the Elastic indices")
    public void prepare() {
        String createCityData
            = StreamUtils.getRawContent(ElasticClientApp.getAppContext(),
                                        R.raw.create_index_cities);
        String createTestCityData
            = StreamUtils.getRawContent(ElasticClientApp.getAppContext(),
                                        R.raw.create_index_testcities);

        client.removeIndices(new String[] {"cities", "testcities"});

        client.createIndex("cities", createCityData);
        client.createIndex("testcities", createTestCityData);

        client.addDocument("cities", "city", "karcag", new SimpleCity("Karcag"));
        client.addDocument("cities", "city", "budapest", new SimpleCity("Budapest"));

        client.addDocument("testcities", "testcity", "customCity", new SimpleCity("customCityForTesting"));
    }
}
