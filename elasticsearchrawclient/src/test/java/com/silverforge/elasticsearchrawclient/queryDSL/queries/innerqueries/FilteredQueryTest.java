package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.queryDSL.filters.innerfilters.MatchAllFilter;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.StrategyOperator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class FilteredQueryTest {

    // region Happy path

    @Test
    public void when_only_query_is_defined_then_query_string_generated_well() {
        FilteredQuery query = FilteredQuery
            .builder()
            .query(MatchQuery
                .builder()
                .build())
            .strategy(StrategyOperator.LEAP_FROG_FILTER_FIRST)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"filtered\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"query\":{\"match\":{\"_all\":\"\"}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"strategy\":\"leap_frog_filter_first\""), greaterThan(0));
    }

    @Test
    public void when_both_query_and_filter_are_defined_then_query_string_generated_well() {
        FilteredQuery query = FilteredQuery
            .builder()
            .query(MatchQuery
                .builder()
                .build())
            .filter(
                MatchAllFilter
                    .builder()
                    .build())
            .strategy(StrategyOperator.LEAP_FROG_FILTER_FIRST)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"filtered\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"filter\":{\"constant_score\":{\"filter\":{\"match_all\":{}}}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"query\":{\"match\":{\"_all\":\"\"}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"strategy\":\"leap_frog_filter_first\""), greaterThan(0));
    }

    @Test
    public void when_only_filter_is_defined_then_query_string_generated_well() {
        FilteredQuery query = FilteredQuery
            .builder()
            .filter(
                MatchAllFilter
                    .builder()
                    .build())
            .strategy(StrategyOperator.LEAP_FROG_FILTER_FIRST)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"filtered\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"filter\":{\"constant_score\":{\"filter\":{\"match_all\":{}}}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"query\":{"), is(-1));
        assertThat(queryString.indexOf("\"strategy\":\"leap_frog_filter_first\""), greaterThan(0));
    }

    @Test
    public void when_random_access_N_is_defined_then_query_string_generated_well() {
        FilteredQuery query = FilteredQuery
            .builder()
            .filter(
                MatchAllFilter
                .builder()
                .build())
            .strategy(StrategyOperator.RANDOM_ACCESS_N, 3)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"filtered\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"strategy\":\"random_access_3\""), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test
    public void when_no_params_defined_then_minimum_query_is_generated_well() {
        FilteredQuery query = FilteredQuery
            .builder()
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString, is("{\"filtered\":{}}"));
    }

    // endregion
}
