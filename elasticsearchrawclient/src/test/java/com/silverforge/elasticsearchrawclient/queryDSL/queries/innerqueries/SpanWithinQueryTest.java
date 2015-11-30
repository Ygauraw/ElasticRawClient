package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.definition.QueryTest;

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
public class SpanWithinQueryTest {

    // region Happy path

    @Test
    public void when_minimal_requred_params_added_then_query_is_formatted_well()
            throws MandatoryParametersAreMissingException {

        SpanTermQuery little = mock(SpanTermQuery.class);
        when(little.getQueryString()).thenReturn("{\"span_term\":{\"cities\":\"Karcag\"}}");

        SpanTermQuery big = mock(SpanTermQuery.class);
        when(big.getQueryString()).thenReturn("{\"span_term\":{\"cities\":\"Budapest\"}}");

        SpanWithinQuery query = SpanWithinQuery
                .builder()
                .little(little)
                .big(big)
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"span_within\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

    }

    @Test
    public void when_minimal_requred_params_added_then_query_is_generated_well()
            throws MandatoryParametersAreMissingException {

        SpanTermQuery little = mock(SpanTermQuery.class);
        when(little.getQueryString()).thenReturn("{\"span_term\":{\"cities\":\"Karcag\"}}");

        SpanTermQuery big = mock(SpanTermQuery.class);
        when(big.getQueryString()).thenReturn("{\"span_term\":{\"cities\":\"Budapest\"}}");

        SpanWithinQuery query = SpanWithinQuery
                .builder()
                .little(little)
                .big(big)
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"little\":{\"span_term\":{\"cities\":\"Karcag\"}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"big\":{\"span_term\":{\"cities\":\"Budapest\"}}"), greaterThan(0));

    }

    // endregion Happy path

    // region Sad path

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_params_added_then_exception_is_thrown()
            throws MandatoryParametersAreMissingException {

        SpanWithinQuery
                .builder()
                .build();
    }

    // endregion Sad path

}
