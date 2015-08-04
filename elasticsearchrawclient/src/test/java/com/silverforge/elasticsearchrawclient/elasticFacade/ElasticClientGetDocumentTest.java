package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.util.Log;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.connector.ConnectorSettings;
import com.silverforge.elasticsearchrawclient.elasticFacade.mappers.ElasticClientMapper;
import com.silverforge.elasticsearchrawclient.testModel.City;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.net.URISyntaxException;
import java.util.List;

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
        String[] docIds = {
                "karcag",
                "customCity"};

        List<City> cities = client.getDocument(docIds, City.class);

        assertThat(cities, notNullValue());
        assertThat(cities.size(), greaterThan(0));
    }

    @Test
    public void getDocumentAndMapTest() {
        String[] docIds = {
                "karcag",
                "customCity"};

        List<City> cities = client.getDocument(docIds, City.class);

        assertThat(cities, is(notNullValue()));
        assertThat(cities.size(), equalTo(1));
        assertThat(cities.get(0).getName(), is("Karcag"));
    }

    @Test
    public void getDocumentWithoutIndexTest() {
        String[] docIds ={
                "karcag",
                "customCity"};

        ConnectorSettings customSettings = ConnectorSettings
                .builder()
                .baseUrl(ELASTIC_URL)
                .userName(ELASTIC_APIKEY)
                .build();
        try {
            ElasticClient customClient = new ElasticClient(customSettings);

            List<City> cities = customClient.getDocument(docIds, City.class);

            assertThat(cities, is(notNullValue()));
            assertThat(cities.size(), equalTo(2));
            assertThat(cities, hasItem(Matchers.<City>hasProperty("name", equalTo("Karcag"))));
            assertThat(cities, hasItem(Matchers.<City>hasProperty("name", equalTo("customCityForTesting"))));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void getDocumentWithTypeTest() {
        String[] docIds ={
                "karcag",
                "customCity"};

        ConnectorSettings customSettings = ConnectorSettings
                .builder()
                .baseUrl(ELASTIC_URL)
                .userName(ELASTIC_APIKEY)
                .build();
        try {
            ElasticClient customClient = new ElasticClient(customSettings);

            List<City> cities = customClient.getDocument("city", docIds, City.class);

            assertThat(cities, is(notNullValue()));
            assertThat(cities.size(), equalTo(1));
            assertThat(cities, hasItem(Matchers.<City>hasProperty("name", equalTo("Karcag"))));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    // endregion

    // region Sad path

    @Test
    public void getDocumetsWithNullMap() {
        String[] docIds = null;
        List<City> cities = client.getDocument(docIds, City.class);

        assertThat(cities, notNullValue());
        assertThat(cities.size(), equalTo(0));
    }

    @Test
    public void getDocumetsWithEmptyMap() {
        String[] docIds = {""};
        List<City> cities = client.getDocument(docIds, City.class);

        assertThat(cities, notNullValue());
        assertThat(cities.size(), equalTo(0));
    }

    // endregion
}
