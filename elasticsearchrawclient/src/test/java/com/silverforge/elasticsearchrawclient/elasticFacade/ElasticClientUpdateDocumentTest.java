package com.silverforge.elasticsearchrawclient.elasticFacade;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.IndexCannotBeNullException;
import com.silverforge.elasticsearchrawclient.exceptions.TypeCannotBeNullException;
import com.silverforge.elasticsearchrawclient.testModel.SimpleCity;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ElasticClientUpdateDocumentTest extends ElasticClientBaseTest {

    // region Happy path

    @Test
    public void updateDocumentTest() {
        try {
            String cityId = "karcag";

            SimpleCity city = new SimpleCity("karcagTest");
            client.updateDocument(cityId, city);

            Thread.sleep(1000);

            List<SimpleCity> cities = client.getDocument(new String[]{cityId}, SimpleCity.class);

            assertThat(cities, not(nullValue()));
            assertThat(cities.size(), equalTo(1));
            assertThat(cities, hasItem(Matchers.<SimpleCity>hasProperty("name", equalTo("karcagTest"))));

            SimpleCity retCity = new SimpleCity("Karcag");

            client.updateDocument(cityId, retCity);

            Thread.sleep(1000);

            List<SimpleCity> retCities = client.getDocument(new String[]{cityId}, SimpleCity.class);

            assertThat(retCities, not(nullValue()));
            assertThat(retCities.size(), equalTo(1));
            assertThat(retCities, hasItem(Matchers.<SimpleCity>hasProperty("name", equalTo("Karcag"))));
        } catch (IndexCannotBeNullException | TypeCannotBeNullException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void updateDocumentIndexTest() {
        try {
            String cityId = "customCity";
            SimpleCity city = new SimpleCity("customCityForTestingTest");
            client.updateDocument("testcities", "testcity", cityId, city);

            Thread.sleep(1000);

            List<SimpleCity> cities = client.getDocument("testcities", "testcity", new String[]{cityId}, SimpleCity.class);

            assertThat(cities, not(nullValue()));
            assertThat(cities.size(), equalTo(1));
            assertThat(cities, hasItem(Matchers.<SimpleCity>hasProperty("name", equalTo("customCityForTestingTest"))));

            SimpleCity retCity = new SimpleCity("customCityForTesting");
            client.updateDocument("testcities", "testcity", cityId, retCity);

            Thread.sleep(1000);

            List<SimpleCity> retCities = client.getDocument("testcities", "testcity", new String[]{cityId}, SimpleCity.class);

            assertThat(retCities, not(nullValue()));
            assertThat(retCities.size(), equalTo(1));
            assertThat(retCities, hasItem(Matchers.<SimpleCity>hasProperty("name", equalTo("customCityForTesting"))));
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    // endregion
}
