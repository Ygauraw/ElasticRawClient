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

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = QueryTest.class)
public class BoostingQueryTest {

    // region Happy path

    @Test
    public void when_query_fully_defined_then_query_generated_with_positive_negative() {

        Queryable matchQueryable = mock(Queryable.class);
        when(matchQueryable.getQueryString()).thenReturn("{\"match\":{\"name\":\"Karcag\"}}");

        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        Queryable multiMatchQueryable = mock(Queryable.class);
        when(multiMatchQueryable.getQueryString()).thenReturn("{\"multi_match\":{\"fields\":[\"name\",\"population\"],\"query\":\"Karcag Budapest\",\"use_dis_max\":\"false\",\"analyzer\":\"standard\",\"tie_breaker\":\"0.4\"}}");

        Queryable complicatedMatchQueryable = mock(Queryable.class);
        when(complicatedMatchQueryable.getQueryString()).thenReturn("{\"match\":{\"name\":{\"query\":\"Karcag Budapest\",\"operator\":\"or\",\"analyzer\":\"keyword\"}}}");

        BoostingQuery query = BoostingQuery
            .builder()
            .positive(
                matchAllQueryable,
                matchQueryable)
            .negative(
                complicatedMatchQueryable)
            .negativeBoost(ZeroToOneRangeOperator._0_6)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"boosting\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"positive\":["), greaterThan(0));
        assertThat(queryString.indexOf("\"negative\":["), greaterThan(0));

        assertThat(queryString.indexOf("\"negative_boost\":\"0.6\""), greaterThan(0));
    }

    @Test
    public void when_positive_defined_then_query_generated_with_positive() {
        Queryable matchQueryable = mock(Queryable.class);
        when(matchQueryable.getQueryString()).thenReturn("{\"match\":{\"name\":\"Karcag\"}}");

        BoostingQuery query = BoostingQuery
            .builder()
            .positive(matchQueryable)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"boosting\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"positive\":[{\"match\":{\"name\":\"Karcag\"}}]"), greaterThan(0));
    }

    @Test
    public void when_negative_defined_then_query_generated_with_negative() {
        Queryable matchQueryable = mock(Queryable.class);
        when(matchQueryable.getQueryString()).thenReturn("{\"match\":{\"name\":\"Karcag\"}}");

        BoostingQuery query = BoostingQuery
            .builder()
            .negative(matchQueryable)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"boosting\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"negative\":[{\"match\":{\"name\":\"Karcag\"}}]"), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test
    public void when_no_parameters_defined_then_empty_query_string_is_generated() {
        BoostingQuery query = BoostingQuery
            .builder()
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString, is("{\"boosting\":{}}"));
    }

    // endregion
}
