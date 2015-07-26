package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.util.Log;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.elasticFacade.mappers.ElasticClientMapper;
import com.silverforge.elasticsearchrawclient.testModel.City;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.silverforge.elasticsearchrawclient.utils.StringUtils.generateUUID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
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
        String id = client.addDocument(city);

        assertThat(id, notNullValue());
        assertThat(id, not(""));

        try {
            String document = client.getDocumentById(new String[]{id});
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

    // endregion

    // region Sad path

    // endregion

}
