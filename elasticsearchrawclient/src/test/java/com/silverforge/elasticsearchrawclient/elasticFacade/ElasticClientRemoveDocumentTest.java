package com.silverforge.elasticsearchrawclient.elasticFacade;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.ElasticClientApp;
import com.silverforge.elasticsearchrawclient.R;
import com.silverforge.elasticsearchrawclient.exceptions.IndexCannotBeNullException;
import com.silverforge.elasticsearchrawclient.exceptions.TypeCannotBeNullException;
import com.silverforge.elasticsearchrawclient.testModel.SimpleCity;
import com.silverforge.elasticsearchrawclient.utils.StreamUtils;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
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

            SimpleCity testCity = new SimpleCity("mydelcity");
            String retId = client.addDocument(docId, testCity);
            assertThat(docId, equalTo(retId));

            client.removeDocument(docId);

            Thread.sleep(1000);

            List<SimpleCity> document = client.getDocument(new String[]{docId}, SimpleCity.class);
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

            SimpleCity testCity = new SimpleCity("mydelcity");
            String retId = client.addDocument("testcities", "testcity", docId, testCity);
            assertThat(docId, equalTo(retId));

            client.removeDocument("testcities", "testcity", docId);

            List<SimpleCity> document = client.getDocument(new String[]{docId}, SimpleCity.class);
            assertThat(document, not(nullValue()));
            assertThat(document.size(), equalTo(0));
        } catch (IndexCannotBeNullException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    @Ignore
    public void removeDocumentsQueryTest() {
        try {
            String cityName = "city";
            SimpleCity city1 = new SimpleCity(cityName);
            SimpleCity city2 = new SimpleCity(cityName);
            client.addDocument(city1);
            client.addDocument(city2);

            Thread.sleep(1000);

            String removeCityData
                = StreamUtils.getRawContent(ElasticClientApp.getAppContext(),
                                            R.raw.remove_cities_query);
            String query = removeCityData.replace("{{CITYNAME}}", cityName);

            List<SimpleCity> initialSearch = client.search(query, SimpleCity.class);
            assertThat(initialSearch.size(), equalTo(2));

            client.removeDocumentsQuery(query);

            List<SimpleCity> search = client.search(query, SimpleCity.class);
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
    @Ignore
    public void removeDocumentsQueryIndexTest() {
        try {
            String cityName = "city";
            SimpleCity city1 = new SimpleCity(cityName);
            SimpleCity city2 = new SimpleCity(cityName);
            client.addDocument("testcities", "testcity", "city1", city1);
            client.addDocument("testcities", "testcity", "city2", city2);

            Thread.sleep(1000);

            String removeCityData
                = StreamUtils.getRawContent(ElasticClientApp.getAppContext(),
                                            R.raw.remove_cities_query);
            String query = removeCityData.replace("{{CITYNAME}}", cityName);

            List<SimpleCity> initialSearch = client.search("testcities", query, SimpleCity.class);
            assertThat(initialSearch.size(), equalTo(2));

            client.removeDocumentsQuery(new String[]{"testcities"}, new String[]{"testcity"}, query);

            Thread.sleep(1000);

            List<SimpleCity> search = client.search("testcities", query, SimpleCity.class);
            assertThat(search.size(), equalTo(0));
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    // endregion
}
