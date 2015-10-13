package com.silverforge.elasticsearchrawclient.queryDSL.queries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.MatchQuery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class QueryTest {

    // region Happy path

    @Test
    public void when_inner_query_added_then_query_generated_well() {
        MatchQuery matchQuery = MatchQuery
            .builder()
            .fieldName("name")
            .value("Budapest")
            .build();

        Query query = Query
            .builder()
            .innerQuery(matchQuery)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
        assertThat(queryString, is("{\"query\":{\"match\":{\"name\":\"Budapest\"}}}"));
    }

    @Test
    public void when_inner_query_and_size_added_then_query_generated_well() {
        Query query = Query
            .builder()
            .size(1)
            .innerQuery(MatchQuery
                .builder()
                .fieldName("name")
                .value("Budapest")
                .build())
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
        assertThat(queryString, is("{\"size\":\"1\",\"query\":{\"match\":{\"name\":\"Budapest\"}}}"));
    }

    @Test
    public void when_inner_query_and_from_and_size_added_then_query_generated_well() {
        Query query = Query
            .builder()
            .from(20)
            .size(100)
            .innerQuery(MatchQuery
                .builder()
                .fieldName("name")
                .value("Budapest")
                .build())
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
        assertThat(queryString, is("{\"from\":\"20\",\"size\":\"100\",\"query\":{\"match\":{\"name\":\"Budapest\"}}}"));
    }


    // endregion

    // region Sad path

    // endregion

}
