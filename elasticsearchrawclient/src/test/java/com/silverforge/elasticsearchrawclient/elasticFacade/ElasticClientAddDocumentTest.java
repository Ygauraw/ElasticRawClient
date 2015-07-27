package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.util.Log;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.connector.ConnectorSettings;
import com.silverforge.elasticsearchrawclient.elasticFacade.exceptions.IndexCannotBeNullException;
import com.silverforge.elasticsearchrawclient.elasticFacade.mappers.ElasticClientMapper;
import com.silverforge.elasticsearchrawclient.testModel.City;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.silverforge.elasticsearchrawclient.utils.StringUtils.generateUUID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ElasticClientAddDocumentTest extends ElasticClientBaseTest {
    private static final String TAG = ElasticClientAddDocumentTest.class.getName();
    private ElasticClientMapper<City> cityMapper = new ElasticClientMapper<>();

    // region Happy path

    @Test
    public void addDocumentTest() {
        String cityName = generateUUID();
        City city = new City(cityName);
        String id = null;

        try {
            id = client.addDocument(city);
            System.out.println(String.format("DocumentId : %s", id));
        } catch (IndexCannotBeNullException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }

        assertThat(id, notNullValue());
        assertThat(id, not(""));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            String document = client.getDocument(new String[]{id});
            assertThat(document, not(nullValue()));

            List<City> cities = cityMapper.mapToList(document, City.class);
            assertThat(cities, not(nullValue()));
            assertThat(cities.size(), is(1));
            assertThat(cities.get(0).getName(), is(cityName));
        } catch (NoSuchAlgorithmException | KeyManagementException | IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void addDocumentWithIdTest() {
        String cityName = generateUUID();
        String cityId = generateUUID();
        City city = new City(cityName);

        try {
            String resultId = client.addDocument(city, cityId);
            assertThat(resultId, equalTo(cityId));
        } catch (IndexCannotBeNullException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void addDocumentWithFullPath() {
        String cityName = generateUUID();
        String cityId = generateUUID();
        City city = new City(cityName);

        String resultId = client.addDocument(city, "testcities", "testcity", cityId);;
        assertThat(resultId, equalTo(cityId));
    }

    // endregion

    // region Sad path

    @Test(expected = IllegalArgumentException.class)
    public void addDocumentWithNullTest() {
        try {
            client.addDocument(null);
        } catch (IndexCannotBeNullException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test(expected = IndexCannotBeNullException.class)
    public void addDocumentWithNullIndexTest()
            throws IndexCannotBeNullException {

        ConnectorSettings settings = ConnectorSettings
                .builder()
                .baseUrl(ELASTIC_URL)
                .userName(ELASTIC_APIKEY)
                .build();

        try {
            ElasticClient testClient = new ElasticClient(settings);

            String cityName = generateUUID();
            City city = new City(cityName);
            testClient.addDocument(city);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    @Rule
    public ExpectedException expectedExceptionRuler = ExpectedException.none();

    @Test
    public void addDocumentNullEntityTest() {
        expectedExceptionRuler.expect(IllegalArgumentException.class);
        expectedExceptionRuler.expectMessage("entity cannot be null");

        client.addDocument(null, "a", "b", "c");
    }

    @Test
    public void addDocumentNullIndexTest() {
        expectedExceptionRuler.expect(IllegalArgumentException.class);
        expectedExceptionRuler.expectMessage("index cannot be null or empty");

        client.addDocument(new City("alma"), null, "b", "c");
    }

    @Test
    public void addDocumentNullTypeTest() {
        expectedExceptionRuler.expect(IllegalArgumentException.class);
        expectedExceptionRuler.expectMessage("type cannot be null or empty");

        client.addDocument(new City("alma"), "a", null, "c");
    }

    @Test
    public void addDocumentNullIdTest() {
        expectedExceptionRuler.expect(IllegalArgumentException.class);
        expectedExceptionRuler.expectMessage("id cannot be null or empty");

        client.addDocument(new City("alma"), "a", "b", null);
    }

    @Test
    public void addDocumentEmptyIndexTest() {
        expectedExceptionRuler.expect(IllegalArgumentException.class);
        expectedExceptionRuler.expectMessage("index cannot be null or empty");

        client.addDocument(new City("alma"), "", "b", "c");
    }

    @Test
    public void addDocumentEmptyTypeTest() {
        expectedExceptionRuler.expect(IllegalArgumentException.class);
        expectedExceptionRuler.expectMessage("type cannot be null or empty");

        client.addDocument(new City("alma"), "a", "", "c");
    }

    @Test
    public void addDocumentEmptyIdTest() {
        expectedExceptionRuler.expect(IllegalArgumentException.class);
        expectedExceptionRuler.expectMessage("id cannot be null or empty");

        client.addDocument(new City("alma"), "a", "b", "");
    }

    // endregion
}
