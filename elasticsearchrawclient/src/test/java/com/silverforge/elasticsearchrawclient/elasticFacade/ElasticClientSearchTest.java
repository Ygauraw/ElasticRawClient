package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.util.Log;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.model.ElasticSettings;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.LogicOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Query;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.MatchQuery;
import com.silverforge.elasticsearchrawclient.testModel.SimpleCity;
import com.silverforge.webconnector.exceptions.SettingsIsNullException;
import com.silverforge.webconnector.model.ConnectorSettings;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.net.URISyntaxException;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ElasticClientSearchTest extends ElasticClientBaseTest {
    private static final String TAG = ElasticClientSearchTest.class.getName();

    // region Happy path

    @Test
    public void searchWithIndexWithTypeTest() {
        List<SimpleCity> cities = client.search("{\"query\":{\"match_all\": {}}}", SimpleCity.class);
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
            ElasticRawClient testClient = new ElasticClient(settings);
            List<SimpleCity> cities = testClient.search("{\"query\":{\"match_all\": {}}}", SimpleCity.class);

            assertNotNull(cities);
        } catch (URISyntaxException | SettingsIsNullException e) {
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
                .userName(ELASTIC_APIKEY)
                .build();

        ElasticSettings elasticSettings = ElasticSettings
            .builder()
            .types(new String[]{"city", "testcity"})
            .build();

        try {
            ElasticRawClient testClient = new ElasticClient(settings, elasticSettings);
            List<SimpleCity> cities = testClient.search("{\"query\":{\"match_all\": {}}}", SimpleCity.class);

            assertNotNull(cities);
        } catch (URISyntaxException | SettingsIsNullException e) {
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
                .userName(ELASTIC_APIKEY)
                .build();

        ElasticSettings elasticSettings = ElasticSettings
            .builder()
            .indices(new String[]{"cities", "testcities"})
            .build();

        try {
            ElasticRawClient testClient = new ElasticClient(settings, elasticSettings);
            List<SimpleCity> cities = testClient.search("{\"query\":{\"match_all\": {}}}", SimpleCity.class);

            assertNotNull(cities);
        } catch (URISyntaxException | SettingsIsNullException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void searchAsyncTest() {
        Observable<SimpleCity> cityObservable
                = client
                    .searchAsync("{\"query\":{\"match_all\": {}}}", SimpleCity.class);

        TestSubscriber<SimpleCity> testSubscriber = new TestSubscriber<>();
        cityObservable.subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertUnsubscribed();

        List<SimpleCity> cityList = testSubscriber.getOnNextEvents();
        assertThat(cityList, not(nullValue()));
        assertThat(cityList.size(), greaterThan(0));
    }

    @Test
    public void when_query_builder_used_then_proper_data_retrieved() {

        Query query = Query
            .builder()
            .from(0)
            .size(10)
            .query(MatchQuery
                .builder()
                .fieldName("name")
                .value("Karcag")
                .build())
            .build();

        List<SimpleCity> cities = client.search(query, SimpleCity.class);

        assertThat(cities, notNullValue());
        assertThat(cities.size(), is(1));
        assertThat(cities.get(0).getName(), is("Karcag"));
    }

    @Test
    public void when_query_builder_used_with_multiple_args_then_proper_data_retrieved() {

        Query query = Query
            .builder()
            .from(0)
            .size(10)
            .query(MatchQuery
                .builder()
                .fieldName("name")
                .query("Karcag Budapest")
                .operator(LogicOperator.OR)
                .build())
            .build();

        List<SimpleCity> cities = client.search(query, SimpleCity.class);

        assertThat(cities, notNullValue());
        assertThat(cities.size(), is(2));
    }

    // endregion

    // region Sad path

    @Test
    public void searchEmptyQueryTest() {
        List<SimpleCity> cities = client.search("{}", SimpleCity.class);

        assertNotNull(cities);
    }

    @Test
    public void searchNullTest() {
        List<SimpleCity> cities = client.search((String)null, SimpleCity.class);

        assertNotNull(cities);
    }

    @Test
    public void searchEmptyTest() {
        List<SimpleCity> cities = client.search("", SimpleCity.class);

        assertNotNull(cities);
    }

    // endregion
}
