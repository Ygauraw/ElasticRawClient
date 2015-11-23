package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.QueryTest;
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
public class SpanNearQueryTest {

    // region Happy path

    @Test
    public void when_minimal_requred_params_added_then_query_is_formatted_well()
            throws MandatoryParametersAreMissingException {

        SpanTermQuery spanTermQuery = mock(SpanTermQuery.class);
        when(spanTermQuery.getQueryString()).thenReturn("{\"span_term\":{\"cities\":\"Karcag\"}}");

        SpanNearQuery query = SpanNearQuery
                .builder()
                .clauses(spanTermQuery)
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"span_near\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

    }

    @Test
    public void when_minimal_requred_params_added_then_query_is_generated_well()
            throws MandatoryParametersAreMissingException {

        SpanTermQuery spanTermQuery = mock(SpanTermQuery.class);
        when(spanTermQuery.getQueryString()).thenReturn("{\"span_term\":{\"cities\":\"Karcag\"}}");

        SpanNearQuery query = SpanNearQuery
                .builder()
                .clauses(spanTermQuery)
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"clauses\":[{\"span_term\":{\"cities\":\"Karcag\"}}]"), greaterThan(0));

    }

    @Test
    public void when_all_params_added_then_query_is_generated_well()
            throws MandatoryParametersAreMissingException {

        SpanTermQuery spanTermQuery = mock(SpanTermQuery.class);
        when(spanTermQuery.getQueryString()).thenReturn("{\"span_term\":{\"cities\":\"Karcag\"}}");
        SpanTermQuery spanTermQuery2 = mock(SpanTermQuery.class);
        when(spanTermQuery2.getQueryString()).thenReturn("{\"span_term\":{\"cities\":\"Budapest\"}}");

        SpanNearQuery query = SpanNearQuery
                .builder()
                .clauses(spanTermQuery, spanTermQuery2)
                .collect_payloads(true)
                .in_order(false)
                .slop(12)
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"clauses\":[{\"span_term\":{\"cities\":\"Karcag\"}},{\"span_term\":{\"cities\":\"Budapest\"}}]"), greaterThan(0));
        assertThat(queryString.indexOf("\"collect_payloads\":\"true\""), greaterThan(0));
        assertThat(queryString.indexOf("\"in_order\":\"false\""), greaterThan(0));
        assertThat(queryString.indexOf("\"slop\":\"12\""), greaterThan(0));

    }

    // endregion Happy path

    // region Sad path

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_params_added_then_exception_is_thrown()
            throws MandatoryParametersAreMissingException {

        SpanNearQuery
                .builder()
                .build();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_clauses_added_then_exception_is_thrown()
            throws MandatoryParametersAreMissingException {

        SpanNearQuery
                .builder()
                .slop(12)
                .collect_payloads(true)
                .in_order(false)
                .build();
    }

    // endregion Sad path

}
