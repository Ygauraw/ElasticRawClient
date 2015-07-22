package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.util.Log;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.connector.ConnectorSettings;
import com.silverforge.elasticsearchrawclient.elasticFacade.Mappers.RawSourceMapTo;
import com.silverforge.elasticsearchrawclient.testModel.City;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ElasticClientGetDocumentTest extends ElasticClientBaseTest {
    private static final String TAG = ElasticClientGetDocumentTest.class.getName();
    private static final String[] IDS ={"4kIU9haiQ4ysvGj4TZe0eQ", "y5llcxGFSqCsItk3VBE-7w"};
    private RawSourceMapTo<City> cityMapper = new RawSourceMapTo<>();

    @Test
    public void getDocMapTest() {
        try {
            String documents = client.getDocumentById(IDS);

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
//
//    @Test
//    public void getDocumentTest() {
//        try {
//            String documents = client.getDocumentById(IDS);
//            String documents1 = client.getDocumentById(IDS);
//
//            assertNotNull(documents);
//        } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
//            e.printStackTrace();
//            fail(e.getMessage());
//        }
//    }




//
//    @Test
//    @Ignore
//    public void getDocumentWithoutIndexTest() {
//
//        ConnectorSettings customSettings = ConnectorSettings
//                .builder()
//                .baseUrl(ELASTIC_URL)
//                .userName(ELASTIC_APIKEY)
//                .build();
//        try {
//            ElasticClient customClient = new ElasticClient(customSettings);
//            String documents = customClient.getDocumentById(IDS);
//
//            List<City> cities = cityMapper.mapToList(documents, City.class);
//
//            assertThat(cities, is(notNullValue()));
//            assertThat(cities.size(), equalTo(1));
//            assertThat(cities.get(0).getName(), is("Szentendre"));
//        } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
//            e.printStackTrace();
//            fail(e.getMessage());
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            Log.e(TAG, e.getMessage());
//            fail(e.getMessage());
//        }
//    }
}
