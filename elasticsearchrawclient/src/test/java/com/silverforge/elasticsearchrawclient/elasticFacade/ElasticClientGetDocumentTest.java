package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.util.Log;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.elasticFacade.model.ElasticSettings;
import com.silverforge.elasticsearchrawclient.exceptions.IndexCannotBeNullException;
import com.silverforge.elasticsearchrawclient.testModel.City;
import com.silverforge.webconnector.exceptions.SettingsIsNullException;
import com.silverforge.webconnector.model.ConnectorSettings;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.net.URISyntaxException;
import java.util.List;

import rx.observers.TestSubscriber;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ElasticClientGetDocumentTest extends ElasticClientBaseTest {
    private static final String TAG = ElasticClientGetDocumentTest.class.getName();

    // region Happy path

    @Test
    public void getDocumentTest() {
        try {
            String[] docIds = {
                    "karcag",
                    "customCity"};

            List<City> cities = client.getDocument(docIds, City.class);

            assertThat(cities, notNullValue());
            assertThat(cities.size(), greaterThan(0));
        } catch (IndexCannotBeNullException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void getDocumentAndMapTest() {
        try {
            String[] docIds = {
                    "karcag",
                    "customCity"};

            List<City> cities = client.getDocument(docIds, City.class);

            assertThat(cities, is(notNullValue()));
            assertThat(cities.size(), equalTo(1));
            assertThat(cities.get(0).getName(), is("Karcag"));
        } catch (IndexCannotBeNullException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void getDocumentMultipleIndicesTest() {
        String[] docIds ={
                "karcag",
                "customCity"};

        ConnectorSettings customSettings = ConnectorSettings
                .builder()
                .baseUrl(ELASTIC_URL)
                .userName(ELASTIC_APIKEY)
                .build();

        ElasticSettings elasticSettings = ElasticSettings
            .builder()
            .indices(new String[]{"cities", "testcities"})
            .build();

        try {
            ElasticRawClient customClient = new ElasticClient(customSettings, elasticSettings);

            List<City> cities = customClient.getDocument(docIds, City.class);

            assertThat(cities, is(notNullValue()));
            assertThat(cities.size(), equalTo(2));
            assertThat(cities, hasItem(Matchers.<City>hasProperty("name", equalTo("Karcag"))));
            assertThat(cities, hasItem(Matchers.<City>hasProperty("name", equalTo("customCityForTesting"))));
        } catch (URISyntaxException | IndexCannotBeNullException | SettingsIsNullException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void getDocumentMultipleIndicesWithTypeTest() {
        String[] docIds ={
                "karcag",
                "customCity"};

        ConnectorSettings customSettings = ConnectorSettings
                .builder()
                .baseUrl(ELASTIC_URL)
                .userName(ELASTIC_APIKEY)
                .build();

        ElasticSettings elasticSettings = ElasticSettings
            .builder()
            .indices(new String[]{"cities", "testcities"})
            .build();

        try {
            ElasticRawClient customClient = new ElasticClient(customSettings, elasticSettings);

            List<City> cities = customClient.getDocument("city", docIds, City.class);

            assertThat(cities, is(notNullValue()));
            assertThat(cities.size(), equalTo(1));
            assertThat(cities, hasItem(Matchers.<City>hasProperty("name", equalTo("Karcag"))));
        } catch (URISyntaxException | IndexCannotBeNullException | SettingsIsNullException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void getDocumentAsyncTest() {
        String[] docIds ={
                "karcag",
                "customCity"};

        TestSubscriber<City> testSubscriber = new TestSubscriber<>();
        client
            .getDocumentAsync(docIds, City.class)
            .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertUnsubscribed();

        List<City> cities = testSubscriber.getOnNextEvents();
        assertThat(cities, notNullValue());
        assertThat(cities.size(), equalTo(1));
        assertThat(cities.get(0).getName(), is("Karcag"));
    }

    @Test
    public void getDocumentAsyncMultipleIndicesTest() {
        try {
            String[] docIds ={
                    "karcag",
                    "customCity"};

            ConnectorSettings customSettings = ConnectorSettings
                    .builder()
                    .baseUrl(ELASTIC_URL)
                    .userName(ELASTIC_APIKEY)
                    .build();

            ElasticSettings elasticSettings = ElasticSettings
                .builder()
                .indices(new String[]{"cities", "testcities"})
                .build();

            ElasticRawClient customClient = new ElasticClient(customSettings, elasticSettings);

            TestSubscriber<City> testSubscriber = new TestSubscriber<>();

            customClient
                    .getDocumentAsync(docIds, City.class)
                    .subscribe(testSubscriber);

            testSubscriber.assertNoErrors();
            testSubscriber.assertUnsubscribed();

            List<City> cities = testSubscriber.getOnNextEvents();

            assertThat(cities, is(notNullValue()));
            assertThat(cities.size(), equalTo(2));
            assertThat(cities, hasItem(Matchers.<City>hasProperty("name", equalTo("Karcag"))));
            assertThat(cities, hasItem(Matchers.<City>hasProperty("name", equalTo("customCityForTesting"))));
        } catch (URISyntaxException | SettingsIsNullException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void getDocumentAsyncMultipleIndicesWithTypeTest() {
        try {
            String[] docIds ={
                    "karcag",
                    "customCity"};

            ConnectorSettings customSettings = ConnectorSettings
                    .builder()
                    .baseUrl(ELASTIC_URL)
                    .userName(ELASTIC_APIKEY)
                    .build();

            ElasticSettings elasticSettings = ElasticSettings
                .builder()
                .indices(new String[]{"cities", "testcities"})
                .build();

            ElasticRawClient customClient = new ElasticClient(customSettings, elasticSettings);

            TestSubscriber<City> testSubscriber = new TestSubscriber<>();

            customClient
                    .getDocumentAsync("city", docIds, City.class)
                    .subscribe(testSubscriber);

            testSubscriber.assertNoErrors();
            testSubscriber.assertUnsubscribed();

            List<City> cities = testSubscriber.getOnNextEvents();

            assertThat(cities, is(notNullValue()));
            assertThat(cities.size(), equalTo(1));
            assertThat(cities, hasItem(Matchers.<City>hasProperty("name", equalTo("Karcag"))));
        } catch (URISyntaxException | SettingsIsNullException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    // endregion

    // region Sad path

    @Test(expected = IndexCannotBeNullException.class)
    public void getDocumentWithoutIndexTest() throws IndexCannotBeNullException {
        String[] docIds ={
                "karcag",
                "customCity"};

        ConnectorSettings customSettings = ConnectorSettings
                .builder()
                .baseUrl(ELASTIC_URL)
                .userName(ELASTIC_APIKEY)
                .build();
        try {
            ElasticRawClient customClient = new ElasticClient(customSettings);

            customClient.getDocument(docIds, City.class);
        } catch (URISyntaxException | SettingsIsNullException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test(expected = IndexCannotBeNullException.class)
    public void getDocumentWithTypeTest() throws IndexCannotBeNullException {
        String[] docIds ={
                "karcag",
                "customCity"};

        ConnectorSettings customSettings = ConnectorSettings
                .builder()
                .baseUrl(ELASTIC_URL)
                .userName(ELASTIC_APIKEY)
                .build();
        try {
            ElasticRawClient customClient = new ElasticClient(customSettings);

            List<City> cities = customClient.getDocument("city", docIds, City.class);

        } catch (URISyntaxException | SettingsIsNullException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void getDocumentWithDashDTypeTest() throws IndexCannotBeNullException {
        String[] docIds ={
                "karcag",
                "customCity"};

        ConnectorSettings customSettings = ConnectorSettings
                .builder()
                .baseUrl(ELASTIC_URL)
                .userName(ELASTIC_APIKEY)
                .build();

        ElasticSettings elasticSettings = ElasticSettings
            .builder()
            .indices(new String[]{"cities"})
            .build();

        try {
            ElasticRawClient customClient = new ElasticClient(customSettings, elasticSettings);

            List<City> cities = customClient.getDocument(" -d", docIds, City.class);

            assertThat(cities, notNullValue());
            assertThat(cities.size(), equalTo(0));
        } catch (URISyntaxException | SettingsIsNullException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void getDocumetsWithNullMap() {
        try {
            String[] docIds = null;
            List<City> cities = client.getDocument(docIds, City.class);

            assertThat(cities, notNullValue());
            assertThat(cities.size(), equalTo(0));
        } catch (IndexCannotBeNullException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void getDocumetsWithEmptyMap() {
        try {
            String[] docIds = {""};
            List<City> cities = client.getDocument(docIds, City.class);

            assertThat(cities, notNullValue());
            assertThat(cities.size(), equalTo(0));
        } catch (IndexCannotBeNullException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    // endregion
}
