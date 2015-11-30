package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.util.Log;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.IndexCannotBeNullException;
import com.silverforge.elasticsearchrawclient.exceptions.TypeCannotBeNullException;
import com.silverforge.elasticsearchrawclient.testModel.SimpleCity;
import com.silverforge.webconnector.exceptions.SettingsIsNullException;
import com.silverforge.webconnector.model.ConnectorSettings;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.net.URISyntaxException;
import java.util.List;

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

    // region Happy path

    @Test
    public void addDocumentTest() {
        String cityName = generateUUID();
        SimpleCity city = new SimpleCity(cityName);
        String id = null;

        try {
            id = client.addDocument(city);
            System.out.println(String.format("DocumentId : %s", id));

            assertThat(id, notNullValue());
            assertThat(id, not(""));

            Thread.sleep(1000);

            List<SimpleCity> cities = client.getDocument(new String[]{id}, SimpleCity.class);
            assertThat(cities, not(nullValue()));
            assertThat(cities.size(), is(1));
            assertThat(cities.get(0).getName(), is(cityName));

        } catch (IndexCannotBeNullException | TypeCannotBeNullException |InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void addDocumentWithIdTest() {
        String cityName = generateUUID();
        String cityId = generateUUID();
        SimpleCity city = new SimpleCity(cityName);

        try {
            String resultId = client.addDocument(cityId, city);
            assertThat(resultId, equalTo(cityId));
        } catch (IndexCannotBeNullException | TypeCannotBeNullException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void addDocumentWithFullPath() {
        String cityName = generateUUID();
        String cityId = generateUUID();
        SimpleCity city = new SimpleCity(cityName);

        String resultId = client.addDocument("testcities", "testcity", cityId, city);;
        assertThat(resultId, equalTo(cityId));
    }

    // endregion

    // region Sad path

    @Test(expected = IllegalArgumentException.class)
    public void addDocumentWithNullTest() {
        try {
            client.addDocument(null);
        } catch (IndexCannotBeNullException | TypeCannotBeNullException e) {
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
            ElasticRawClient testClient = new ElasticClient(settings);

            String cityName = generateUUID();
            SimpleCity city = new SimpleCity(cityName);
            testClient.addDocument(city);
        } catch (URISyntaxException | TypeCannotBeNullException | SettingsIsNullException e) {
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

        client.addDocument("a", "b", "c", null);
    }

    @Test
    public void addDocumentNullIndexTest() {
        expectedExceptionRuler.expect(IllegalArgumentException.class);
        expectedExceptionRuler.expectMessage("index cannot be null or empty");

        client.addDocument(null, "b", "c", new SimpleCity("alma"));
    }

    @Test
    public void addDocumentNullTypeTest() {
        expectedExceptionRuler.expect(IllegalArgumentException.class);
        expectedExceptionRuler.expectMessage("type cannot be null or empty");

        client.addDocument("a", null, "c", new SimpleCity("alma"));
    }

    @Test
    public void addDocumentNullIdTest() {
        expectedExceptionRuler.expect(IllegalArgumentException.class);
        expectedExceptionRuler.expectMessage("id cannot be null or empty");

        client.addDocument("a", "b", null, new SimpleCity("alma"));
    }

    @Test
    public void addDocumentEmptyIndexTest() {
        expectedExceptionRuler.expect(IllegalArgumentException.class);
        expectedExceptionRuler.expectMessage("index cannot be null or empty");

        client.addDocument("", "b", "c", new SimpleCity("alma"));
    }

    @Test
    public void addDocumentEmptyTypeTest() {
        expectedExceptionRuler.expect(IllegalArgumentException.class);
        expectedExceptionRuler.expectMessage("type cannot be null or empty");

        client.addDocument("a", "", "c", new SimpleCity("alma"));
    }

    @Test
    public void addDocumentEmptyIdTest() {
        expectedExceptionRuler.expect(IllegalArgumentException.class);
        expectedExceptionRuler.expectMessage("id cannot be null or empty");

        client.addDocument("a", "b", "", new SimpleCity("alma"));
    }

    // endregion
}
