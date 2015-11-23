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
public class AndQueryTest {

    // region Happy path

    @Test
    public void when_minimal_requred_params_added_then_query_is_formatted_well() throws MandatoryParametersAreMissingException {

        TermQuery firstFilter = mock(TermQuery.class);
        when(firstFilter.getQueryString()).thenReturn("{\"term\":{\"cities\":\"Budapest\"}}");

        TermQuery secondFilter = mock(TermQuery.class);
        when(secondFilter.getQueryString()).thenReturn("{\"term\":{\"cities\":\"Karcag\"}}");

        AndQuery query = AndQuery
                .builder()
                .filters(firstFilter, secondFilter)
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"and\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

    }

    @Test
    public void when_minimal_requred_params_added_then_query_is_generated_well() throws MandatoryParametersAreMissingException {

        TermQuery firstFilter = mock(TermQuery.class);
        when(firstFilter.getQueryString()).thenReturn("{\"term\":{\"cities\":\"Budapest\"}}");

        TermQuery secondFilter = mock(TermQuery.class);
        when(secondFilter.getQueryString()).thenReturn("{\"term\":{\"cities\":\"Karcag\"}}");

        AndQuery query = AndQuery
                .builder()
                .filters(firstFilter, secondFilter)
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"and\":{\"filters\":["), greaterThan(0));
        assertThat(queryString.indexOf("{\"term\":{\"cities\":\"Budapest\"}},"), greaterThan(0));
        assertThat(queryString.indexOf("{\"term\":{\"cities\":\"Karcag\"}}]"), greaterThan(0));

    }

    @Test
    public void when_more_than_two_filters_added_then_query_is_generated_well() throws MandatoryParametersAreMissingException {

        TermQuery firstFilter = mock(TermQuery.class);
        when(firstFilter.getQueryString()).thenReturn("{\"term\":{\"cities\":\"Budapest\"}}");

        TermQuery secondFilter = mock(TermQuery.class);
        when(secondFilter.getQueryString()).thenReturn("{\"term\":{\"cities\":\"Karcag\"}}");

        TermQuery thirdFilter = mock(TermQuery.class);
        when(thirdFilter.getQueryString()).thenReturn("{\"term\":{\"cities\":\"Miskolc\"}}");

        AndQuery query = AndQuery
                .builder()
                .filters(firstFilter, secondFilter, thirdFilter)
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"and\":{\"filters\":["), greaterThan(0));
        assertThat(queryString.indexOf("{\"term\":{\"cities\":\"Budapest\"}},"), greaterThan(0));
        assertThat(queryString.indexOf("{\"term\":{\"cities\":\"Karcag\"}},"), greaterThan(0));
        assertThat(queryString.indexOf("{\"term\":{\"cities\":\"Miskolc\"}}]"), greaterThan(0));

    }

    // endregion Happy path

    // region Sad path

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_params_added_then_exception_is_thrown() throws MandatoryParametersAreMissingException {

        AndQuery query = AndQuery
                .builder()
                .build();

    }

    // endregion Sad path

}
