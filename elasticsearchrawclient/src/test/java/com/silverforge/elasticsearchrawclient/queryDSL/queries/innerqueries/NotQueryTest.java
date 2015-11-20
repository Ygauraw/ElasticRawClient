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
public class NotQueryTest {

    // region Happy path

    @Test
    public void when_minimal_requred_params_added_then_query_is_formatted_well()
            throws MandatoryParametersAreMissingException {

        MatchQuery matchQuery = mock(MatchQuery.class);
        when(matchQuery.getQueryString()).thenReturn("{\"match\":{\"_all\":\"Karcag\"}}");

        NotQuery query = NotQuery
                .builder()
                .query(matchQuery)
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"not\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

    }

    @Test
    public void when_all_params_added_then_query_is_generated_well()
            throws MandatoryParametersAreMissingException {

        MatchQuery matchQuery = mock(MatchQuery.class);
        when(matchQuery.getQueryString()).thenReturn("{\"match\":{\"_all\":\"Karcag\"}}");

        NotQuery query = NotQuery
                .builder()
                .query(matchQuery)
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"not\":{\"query\":{"), greaterThan(0));
        assertThat(queryString.indexOf("\"query\":{\"match\":{\"_all\":\"Karcag\"}}"), greaterThan(0));

    }

    // endregion Happy path

    // region Sad path

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_params_added_then_exception_is_thrown()
            throws MandatoryParametersAreMissingException {

        NotQuery
                .builder()
                .build();
    }

    // endregion Sad path

}
