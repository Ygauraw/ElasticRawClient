package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ScoreModeOperator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class NestedQueryTest {

    // region Happy path

    @Test
    public void when_all_parameters_defined_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        String queryString = NestedQuery
            .builder()
            .path("mypath.somewhere.else")
            .query(MatchAllQuery.builder().build())
            .scoreMode(ScoreModeOperator.MIN)
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"nested\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"path\":\"mypath.somewhere.else\""), greaterThan(0));
        assertThat(queryString.indexOf("\"query\":{\"match_all\":{}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"score_mode\":\"min\""), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test
    public void when_null_path_defined_then_query_generated_well()
        throws MandatoryParametersAreMissingException {

        String queryString = NestedQuery
            .builder()
            .path(null)
            .query(MatchAllQuery.builder().build())
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"nested\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"path\":\"\""), greaterThan(0));
        assertThat(queryString.indexOf("\"query\":{\"match_all\":{}}"), greaterThan(0));
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_path_is_missing_then_exception_thrown()
        throws MandatoryParametersAreMissingException {

        NestedQuery
            .builder()
            .query(MatchAllQuery.builder().build())
            .build()
            .getQueryString();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_query_is_missing_then_exception_thrown()
        throws MandatoryParametersAreMissingException {

        NestedQuery
            .builder()
            .path("blah")
            .build()
            .getQueryString();
    }

    // endregion
}
