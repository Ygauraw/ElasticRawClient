package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.QueryTest;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.LogicOperator;
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
import static org.mockito.Mockito.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = QueryTest.class)
public class BoolQueryTest {

    // region Happy path

    @Test
    public void when_query_fully_defined_then_query_generated_with_must_should_must_not()
            throws MandatoryParametersAreMissingException {

        Queryable matchQueryable = mock(Queryable.class);
        when(matchQueryable.getQueryString()).thenReturn("{\"match\":{\"name\":\"Karcag\"}}");

        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        Queryable multiMatchQueryable = mock(Queryable.class);
        when(multiMatchQueryable.getQueryString()).thenReturn("{\"multi_match\":{\"fields\":[\"name\",\"population\"],\"query\":\"Karcag Budapest\",\"use_dis_max\":\"false\",\"analyzer\":\"standard\",\"tie_breaker\":\"0.4\"}}");

        Queryable complicatedMatchQueryable = mock(Queryable.class);
        when(complicatedMatchQueryable.getQueryString()).thenReturn("{\"match\":{\"name\":{\"query\":\"Karcag Budapest\",\"operator\":\"or\",\"analyzer\":\"keyword\"}}}");

        BoolQuery query = BoolQuery
            .builder()
            .must(
                matchAllQueryable,
                matchQueryable)
            .should(
                multiMatchQueryable)
            .mustNot(
                BoolQuery
                    .builder()
                    .should(
                        complicatedMatchQueryable)
                    .build())
            .minimumShouldMatch(23)
            .disableCoord(false)
            .boost(1.0F)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"bool\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"must\":["), greaterThan(0));
        assertThat(queryString.indexOf("\"must_not\":["), greaterThan(0));
        assertThat(queryString.indexOf("\"should\":["), greaterThan(0));

        assertThat(queryString.indexOf("\"minimum_should_match\":\"23\""), greaterThan(0));
        assertThat(queryString.indexOf("\"disable_coord\":\"false\""), greaterThan(0));
        assertThat(queryString.indexOf("\"boost\":\"1.0\""), greaterThan(0));
    }

    @Test
    public void when_must_defined_then_query_generated_with_must()
            throws MandatoryParametersAreMissingException {

        Queryable matchQueryable = mock(Queryable.class);
        when(matchQueryable.getQueryString()).thenReturn("{\"match\":{\"name\":\"Karcag\"}}");

        BoolQuery query = BoolQuery
            .builder()
            .must(matchQueryable)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"bool\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"must\":[{\"match\":{\"name\":\"Karcag\"}}]"), greaterThan(0));
    }

    @Test
    public void when_must_not_defined_then_query_generated_with_must_not()
            throws MandatoryParametersAreMissingException {

        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        BoolQuery query = BoolQuery
            .builder()
            .mustNot(matchAllQueryable)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"bool\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"must_not\":[{\"match_all\":{}}]"), greaterThan(0));
    }

    @Test
    public void when_should_defined_then_query_generated_with_should()
            throws MandatoryParametersAreMissingException {

        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        BoolQuery query = BoolQuery
            .builder()
            .should(matchAllQueryable)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"bool\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"should\":[{\"match_all\":{}}]"), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test
    public void when_no_parameters_defined_then_empty_query_string_is_generated() {
        BoolQuery query = BoolQuery
            .builder()
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString, is("{\"bool\":{}}"));
    }

    // endregion
}
