package com.silverforge.elasticsearchrawclient.elasticFacade.queries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.definition.QueryIntegrationTest;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Query;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.MatchAllQuery;
import com.silverforge.elasticsearchrawclient.testModel.City;
import com.silverforge.elasticsearchrawclient.testModel.SimpleCity;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(QueryIntegrationTest.class)
public class SearchMatchAllQuery
        extends QueryTestBase {

    @Test
    public void when_match_all_query_executed_then_appropriate_list_retrieved() {

        Query query = Query
            .builder()
            .query(
                MatchAllQuery
                    .builder()
                    .build())
            .build();


        List<City> cities = client.search(query, City.class);

        assertThat(cities, notNullValue());
        assertThat(cities.size(), is(6));
    }
}
