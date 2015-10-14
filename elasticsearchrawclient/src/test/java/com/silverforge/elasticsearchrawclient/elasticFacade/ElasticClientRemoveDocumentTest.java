package com.silverforge.elasticsearchrawclient.elasticFacade;

import android.util.Log;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.ElasticClientApp;
import com.silverforge.elasticsearchrawclient.R;
import com.silverforge.elasticsearchrawclient.exceptions.IndexCannotBeNullException;
import com.silverforge.elasticsearchrawclient.exceptions.TypeCannotBeNullException;
import com.silverforge.elasticsearchrawclient.testModel.City;
import com.silverforge.elasticsearchrawclient.utils.StreamUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ElasticClientRemoveDocumentTest extends ElasticClientBaseTest {

    // region Happy path

    @Test
    public void removeDocumentByIdTest() {
        try {
            String docId = "mydeldoc";

            City testCity = new City("mydelcity");
            String retId = client.addDocument(docId, testCity);
            assertThat(docId, equalTo(retId));

            client.removeDocument(docId);

            Thread.sleep(1000);

            List<City> document = client.getDocument(new String[]{docId}, City.class);
            assertThat(document, not(nullValue()));
            assertThat(document.size(), equalTo(0));
        } catch (IndexCannotBeNullException | TypeCannotBeNullException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void removeDocumentTest() {
        try {
            String docId = "mydeldoc";

            City testCity = new City("mydelcity");
            String retId = client.addDocument("testcities", "testcity", docId, testCity);
            assertThat(docId, equalTo(retId));

            client.removeDocument("testcities", "testcity", docId);

            List<City> document = client.getDocument(new String[]{docId}, City.class);
            assertThat(document, not(nullValue()));
            assertThat(document.size(), equalTo(0));
        } catch (IndexCannotBeNullException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void removeDocumentsQueryTest() {
        try {
            String cityName = "city";
            City city1 = new City(cityName);
            City city2 = new City(cityName);
            client.addDocument(city1);
            client.addDocument(city2);

            Thread.sleep(1000);

            String removeCityData
                = StreamUtils.getRawContent(ElasticClientApp.getAppContext(),
                                            R.raw.remove_cities_query);
            String query = removeCityData.replace("{{CITYNAME}}", cityName);

            List<City> initialSearch = client.search(query, City.class);
            assertThat(initialSearch.size(), equalTo(2));

            client.removeDocumentsQuery(query);

            List<City> search = client.search(query, City.class);
            assertThat(search.size(), equalTo(0));
        } catch (IndexCannotBeNullException | TypeCannotBeNullException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void removeDocumentsQueryIndexTest() {
        try {
            String cityName = "city";
            City city1 = new City(cityName);
            City city2 = new City(cityName);
            client.addDocument("testcities", "testcity", "city1", city1);
            client.addDocument("testcities", "testcity", "city2", city2);

            Thread.sleep(1000);

            String removeCityData
                = StreamUtils.getRawContent(ElasticClientApp.getAppContext(),
                                            R.raw.remove_cities_query);
            String query = removeCityData.replace("{{CITYNAME}}", cityName);

            List<City> initialSearch = client.search("testcities", query, City.class);
            assertThat(initialSearch.size(), equalTo(2));

            client.removeDocumentsQuery(new String[]{"testcities"}, new String[]{"testcity"}, query);

            Thread.sleep(1000);

            List<City> search = client.search("testcities", query, City.class);
            assertThat(search.size(), equalTo(0));
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    // endregion
}
