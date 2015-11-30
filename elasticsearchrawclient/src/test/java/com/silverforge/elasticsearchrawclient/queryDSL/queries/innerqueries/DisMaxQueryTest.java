package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.definition.QueryTest;
import com.silverforge.elasticsearchrawclient.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ZeroToOneRangeOperator;

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
public class DisMaxQueryTest {

    // region Happy path

    @Test
    public void when_queries_defined_then_query_generated_well() {
        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        Queryable commonTermsQueryable = mock(Queryable.class);
        when(commonTermsQueryable.getQueryString()).thenReturn("{\"common\":{\"body\":{\"query\":\"this is bonsai cool\",\"cutoff_frequency\":\"0.001\"}}}");

        DisMaxQuery query = DisMaxQuery
            .builder()
            .queries(
                matchAllQueryable,
                commonTermsQueryable
            )
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"dis_max\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"match_all\":"), greaterThan(0));
        assertThat(queryString.indexOf("\"common\":"), greaterThan(0));
    }

    @Test
    public void when_query_is_fully_defined_then_query_generated_well() {
        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        Queryable commonTermsQueryable = mock(Queryable.class);
        when(commonTermsQueryable.getQueryString()).thenReturn("{\"common\":{\"body\":{\"query\":\"this is bonsai cool\",\"cutoff_frequency\":\"0.001\"}}}");

        DisMaxQuery query = DisMaxQuery
            .builder()
            .queries(
                matchAllQueryable,
                commonTermsQueryable
            )
            .tieBreaker(ZeroToOneRangeOperator._0_8)
            .boost(3)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"dis_max\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"match_all\":"), greaterThan(0));
        assertThat(queryString.indexOf("\"common\":"), greaterThan(0));

        assertThat(queryString.indexOf("\"tie_breaker\":\"0.8\""), greaterThan(0));
        assertThat(queryString.indexOf("\"boost\":\"3\""), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test
    public void when_minimum_query_is_defined_then_query_generated_well() {
        DisMaxQuery query = DisMaxQuery
            .builder()
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"dis_max\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));
    }

    // endregion
}
