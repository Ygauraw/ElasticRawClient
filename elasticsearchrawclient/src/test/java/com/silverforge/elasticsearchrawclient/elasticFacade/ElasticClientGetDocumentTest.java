package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.util.Log;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.connector.ConnectorSettings;
import com.silverforge.elasticsearchrawclient.elasticFacade.Mappers.RawSourceMapTo;
import com.silverforge.elasticsearchrawclient.testModel.City;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ElasticClientGetDocumentTest extends ElasticClientBaseTest {
    private static final String TAG = ElasticClientGetDocumentTest.class.getName();
    private RawSourceMapTo<City> cityMapper = new RawSourceMapTo<>();

    @Test
    public void getDocumentTest() {
        try {
            String[] docIds ={
                    "4kIU9haiQ4ysvGj4TZe0eQ",
                    "y5llcxGFSqCsItk3VBE-7w"};
            String documents = client.getDocumentById(docIds);

            assertThat(documents, notNullValue());
            assertThat(documents, not(""));
        } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void getDocumentAndMapTest() {
        try {
            String[] docIds ={
                    "4kIU9haiQ4ysvGj4TZe0eQ",
                    "y5llcxGFSqCsItk3VBE-7w"};
            String documents = client.getDocumentById(docIds);

            assertThat(documents, notNullValue());
            assertNotEquals(documents, "");

            List<City> cities = cityMapper.mapToList(documents, City.class);

            assertThat(cities, is(notNullValue()));
            assertThat(cities.size(), equalTo(1));
            assertThat(cities.get(0).getName(), is("Szentendre"));
        } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void getDocumentWithoutIndexTest() {
        String[] docIds ={
                "caT_CJqUSaG_6lw1cyNv0w",
                "y5llcxGFSqCsItk3VBE-7w"};

        ConnectorSettings customSettings = ConnectorSettings
                .builder()
                .baseUrl(ELASTIC_URL)
                .userName(ELASTIC_APIKEY)
                .build();
        try {
            ElasticClient customClient = new ElasticClient(customSettings);
            String documents = customClient.getDocumentById(docIds);

            List<City> cities = cityMapper.mapToList(documents, City.class);

            assertThat(cities, is(notNullValue()));
            assertThat(cities.size(), equalTo(2));
//            assertThat(cities.get(0).getName(), is("Szentendre"));
//            assertThat(cities.get(1).getName(), is("customCityForTesting"));

            assertThat(cities, hasItem(Matchers.<City>hasProperty("name", equalTo("Szentendre"))));
            assertThat(cities, hasItem(Matchers.<City>hasProperty("name", equalTo("customCityForTesting"))));

        } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }
}
