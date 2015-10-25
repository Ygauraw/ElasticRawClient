package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.util.Log;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.model.ElasticSettings;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.LogicOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Query;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.MatchQuery;
import com.silverforge.elasticsearchrawclient.testModel.City;
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
            ElasticRawClient testClient = new ElasticClient(settings);
            List<City> cities = testClient.search("{\"query\":{\"match_all\": {}}}", City.class);

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
            List<City> cities = testClient.search("{\"query\":{\"match_all\": {}}}", City.class);

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
            List<City> cities = testClient.search("{\"query\":{\"match_all\": {}}}", City.class);

            assertNotNull(cities);
        } catch (URISyntaxException | SettingsIsNullException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void searchAsyncTest() {
        Observable<City> cityObservable
                = client
                    .searchAsync("{\"query\":{\"match_all\": {}}}", City.class);

        TestSubscriber<City> testSubscriber = new TestSubscriber<>();
        cityObservable.subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertUnsubscribed();

        List<City> cityList = testSubscriber.getOnNextEvents();
        assertThat(cityList, not(nullValue()));
        assertThat(cityList.size(), greaterThan(0));
    }

    @Test
    public void when_query_builder_used_then_proper_data_retrieved() {

        Query query = Query
            .builder()
            .from(0)
            .size(10)
            .innerQuery(MatchQuery
                .builder()
                .fieldName("name")
                .value("Karcag")
                .build())
            .build();

        List<City> cities = client.search(query, City.class);

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
            .innerQuery(MatchQuery
                .builder()
                .fieldName("name")
                .query("Karcag Budapest")
                .operator(LogicOperator.OR)
                .build())
            .build();

        List<City> cities = client.search(query, City.class);

        assertThat(cities, notNullValue());
        assertThat(cities.size(), is(2));
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
        List<City> cities = client.search((String)null, City.class);

        assertNotNull(cities);
    }

    @Test
    public void searchEmptyTest() {
        List<City> cities = client.search("", City.class);

        assertNotNull(cities);
    }

    // endregion
}
