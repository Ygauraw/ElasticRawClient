package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.util.Log;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.ElasticClientApp;
import com.silverforge.elasticsearchrawclient.R;
import com.silverforge.elasticsearchrawclient.connector.ConnectorSettings;
import com.silverforge.elasticsearchrawclient.elasticFacade.model.InvokeResult;
import com.silverforge.elasticsearchrawclient.exceptions.ServerIsNotAvailableException;
import com.silverforge.elasticsearchrawclient.utils.StreamUtils;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ElasticClientCreateIndexTest {
    private static final String TAG = ElasticClientCreateIndexTest.class.getName();
    protected static final String ELASTIC_URL = "https://mgj.east-us.azr.facetflow.io";
    protected static final String ELASTIC_APIKEY = "wihIilbbekmCeppKlgQXDwpSZEUekkk0";
    private final String[] predefinedIndicesForRemove = new String[] {"ti1", "ti2"};
    protected ElasticClient client;

    public ElasticClientCreateIndexTest() {
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
        String indexData
            = StreamUtils.getRawContent(ElasticClientApp.getAppContext(),
                                        R.raw.create_index_testcities);

        String testIndexName = "testindex";

        client.createIndex(testIndexName, indexData);

        boolean indexExists = client.indexExists(testIndexName);
        assertThat(indexExists, is(true));

        client.removeIndices(new String[] {testIndexName});
    }

    @Test
    public void createIndicesBySettingsTest() {
        String indexData
            = StreamUtils.getRawContent(ElasticClientApp.getAppContext(),
                                        R.raw.create_index_testcities);

        client.createIndex(predefinedIndicesForRemove[0], indexData);
        client.createIndex(predefinedIndicesForRemove[1], indexData);

        boolean predefinedIndexOneExists = client.indexExists(predefinedIndicesForRemove[0]);
        boolean predefinedIndexTwoExists = client.indexExists(predefinedIndicesForRemove[1]);
        assertThat(predefinedIndexOneExists, is(true));
        assertThat(predefinedIndexTwoExists, is(true));

        client.removeIndices();
    }

    @Test
    public void addAndRemoveAlias() {
        String aliasName = "myFunnyCities";
        String indexName = "cities";
        client.addAlias(indexName, aliasName);
        List<String> cities = client.getAliases(indexName);

        assertThat(cities, not(nullValue()));
        assertThat(cities.size(), equalTo(1));
        assertThat(cities.get(0), equalTo(aliasName));

        client.removeAlias(indexName, aliasName);
        List<String> retCities = client.getAliases(indexName);

        assertThat(retCities, not(nullValue()));
        assertThat(retCities.size(), equalTo(0));
    }

    // endregion

    // region Sad path

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void wrongIndexCheck()
            throws Exception {

        expectedException.expect(ServerIsNotAvailableException.class);
        expectedException.expectMessage("Server response code : 404");

        InvokeResult head = client.executeRawRequest().head("/thereisnosuchindex");
        assertThat(head.getAggregatedExceptions().size(), greaterThan(0));

        for (Exception exception : head.getAggregatedExceptions()) {
            throw exception;
        }
    }

    // endregion
}
