package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.QueryTest;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = QueryTest.class)
public class IndicesQueryTest {

    // region Happy path

    @Test
    public void when_all_paramaters_defined_then_query_generated_well() {
        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        Queryable commonTermsQueryable = mock(Queryable.class);
        when(commonTermsQueryable.getQueryString()).thenReturn("{\"common\":{\"body\":{\"query\":\"this is bonsai cool\",\"cutoff_frequency\":\"0.001\"}}}");

        String queryString = IndicesQuery
            .builder()
            .indices("apple", "banana", "cherry")
            .query(matchAllQueryable)
            .noMatchQuery(commonTermsQueryable)
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"indices\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"indices\":[\"apple\",\"banana\",\"cherry\"]"), greaterThan(0));
        assertThat(queryString.indexOf("\"query\":{\"match_all\":{}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"no_match_query\":{\"common\":{\"body\":{\"query\":\"this is bonsai cool\",\"cutoff_frequency\":\"0.001\"}}}"), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test
    public void when_no_paramaters_defined_then_minimum_query_generated_well() {
        String queryString = IndicesQuery
            .builder()
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString, is("{\"indices\":{}}"));
    }

    @Test
    public void when_null_index_defined_then_minimum_query_generated_well() {
        String queryString = IndicesQuery
            .builder()
            .indices(null)
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"indices\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"indices\":[]"), greaterThan(0));
        assertThat(queryString.indexOf("\"query\":{"), is(-1));
        assertThat(queryString.indexOf("\"no_match_query\":{"), is(-1));
    }

    //endregion
}
