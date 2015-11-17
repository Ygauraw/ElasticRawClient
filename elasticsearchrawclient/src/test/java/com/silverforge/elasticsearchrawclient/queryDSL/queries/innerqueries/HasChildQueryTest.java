package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.QueryTest;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ScoreModeOperator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = QueryTest.class)
public class HasChildQueryTest {

    // region Happy path

    @Test
    public void when_minimal_requred_params_added_then_query_is_generated_well() throws MandatoryParametersAreMissingException {
        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        HasChildQuery query = HasChildQuery
            .builder()
            .query(matchAllQueryable)
            .type("value")
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"has_child\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"query\":{\"match_all\":{}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"type\":\"value\""), greaterThan(0));
    }

    @Test
    public void when_all_params_added_then_query_is_generated_well() throws MandatoryParametersAreMissingException {
        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        HasChildQuery query = HasChildQuery
            .builder()
            .query(matchAllQueryable)
            .type("value")
            .scoreMode(ScoreModeOperator.MIN)
            .maxChildren(2)
            .minChildren(1)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"has_child\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"query\":{\"match_all\":{}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"type\":\"value\""), greaterThan(0));
        assertThat(queryString.indexOf("\"score_mode\":\"min\""), greaterThan(0));
        assertThat(queryString.indexOf("\"max_children\":\"2\""), greaterThan(0));
        assertThat(queryString.indexOf("\"min_children\":\"1\""), greaterThan(0));
    }

    // endregion Happy path

    // region Sad path

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_params_added_then_exception_is_thrown()
            throws MandatoryParametersAreMissingException {

        HasChildQuery
                .builder()
                .build();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_query_added_then_exception_is_thrown()
            throws MandatoryParametersAreMissingException {

        HasChildQuery query = HasChildQuery
            .builder()
            .type("value")
            .minChildren(1)
            .maxChildren(2)
            .build();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_type_added_then_exception_is_thrown()
            throws MandatoryParametersAreMissingException {

        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        HasChildQuery
            .builder()
            .query(matchAllQueryable)
            .minChildren(1)
            .maxChildren(2)
            .build();
    }

    // endregion Sad path
}
