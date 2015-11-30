package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.definition.QueryTest;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = QueryTest.class)
public class MatchAllQueryTest {

    // region Happy path

    @Test
    public void when_no_parameter_defined_then_query_generated_well() {
        String queryString = MatchAllQuery
            .builder()
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString, is("{\"match_all\":{}}"));
    }

    @Test
    public void when_boost_parameter_defined_then_query_generated_well() {
        String queryString = MatchAllQuery
            .builder()
            .boost(3.4f)
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString, is("{\"match_all\":{\"boost\":\"3.4\"}}"));
    }

    // endregion

    //region Sad path

    // endregion
}
