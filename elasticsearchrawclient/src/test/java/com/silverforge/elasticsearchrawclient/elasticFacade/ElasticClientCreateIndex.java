package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.util.Log;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.ElasticClientApp;
import com.silverforge.elasticsearchrawclient.R;
import com.silverforge.elasticsearchrawclient.connector.ConnectorSettings;
import com.silverforge.elasticsearchrawclient.exceptions.ServerIsNotAvailableException;
import com.silverforge.elasticsearchrawclient.utils.StreamUtils;

import org.bouncycastle.crypto.engines.CamelliaLightEngine;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
public class ElasticClientCreateIndex {
    private static final String TAG = ElasticClientCreateIndex.class.getName();
    protected static final String ELASTIC_URL = "https://silverforge.east-us.azr.facetflow.io";
    protected static final String ELASTIC_APIKEY = "ZjjnkNMgh0uj5yCFIvYVGQsueESCLj1k";
    private final String[] predefinedIndicesForRemove = new String[] {"ti1", "ti2"};
    protected ElasticClient client;

    public ElasticClientCreateIndex() {
        ConnectorSettings settings = ConnectorSettings
                .builder()
                .baseUrl(ELASTIC_URL)
                .indices(predefinedIndicesForRemove)
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
    public void createIndicesTest() {
        InputStream inputStream = ElasticClientApp
                .getAppContext()
                .getResources()
                .openRawResource(R.raw.create_index_testcities);

        String indexData = StreamUtils.convertStreamToString(inputStream);

        try {
            String testIndexName = "testindex";

            client.createIndex(testIndexName, indexData);

            client.raw.head("/" + testIndexName);

            client.removeIndices(new String[] {testIndexName});

        } catch (NoSuchAlgorithmException | KeyManagementException | IOException | ServerIsNotAvailableException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void createIndicesBySettingsTest() {
        InputStream inputStream = ElasticClientApp
                .getAppContext()
                .getResources()
                .openRawResource(R.raw.create_index_testcities);

        String indexData = StreamUtils.convertStreamToString(inputStream);

        try {
            client.createIndex("ti1", indexData);
            client.createIndex("ti2", indexData);

            client.raw.head("/ti1");
            client.raw.head("/ti2");

            client.removeIndices();

        } catch (NoSuchAlgorithmException | KeyManagementException | IOException | ServerIsNotAvailableException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    // endregion

    // region Sad path

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void wrongIndexCheck()
            throws ServerIsNotAvailableException, KeyManagementException, NoSuchAlgorithmException, IOException {

        expectedException.expect(ServerIsNotAvailableException.class);
        expectedException.expectMessage("Server response code : 404");

        client.raw.head("/thereisnosuchindex");
    }

    // endregion
}
