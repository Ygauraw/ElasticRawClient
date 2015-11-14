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
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = QueryTest.class)
public class WildCardQueryTest {

    // region Happy path

    @Test
    public void when_minimal_requred_params_added_then_query_is_generated_well()
            throws MandatoryParametersAreMissingException {

        WildCardQuery query = WildCardQuery
            .builder()
            .fieldName("cities")
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"wildcard\":{\"cities\":\"\"}"), greaterThan(0));
    }

    @Test
    public void when_wildcard_params_added_then_query_is_generated_well()
            throws MandatoryParametersAreMissingException {

        WildCardQuery query = WildCardQuery
            .builder()
            .fieldName("cities")
            .boost(2.4f)
            .wildcard("k.*")
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"wildcard\":{\"cities\":{"), greaterThan(0));
        assertThat(queryString.indexOf("\"wildcard\":\"k.*\""), greaterThan(0));
        assertThat(queryString.indexOf("\"boost\":\"2.4\""), greaterThan(0));
    }

    @Test
    public void when_value_added_then_query_is_generated_well()
            throws MandatoryParametersAreMissingException {

        WildCardQuery query = WildCardQuery
            .builder()
            .fieldName("cities")
            .boost(2)
            .value("k.*")
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"wildcard\":{\"cities\":{"), greaterThan(0));
        assertThat(queryString.indexOf("\"value\":\"k.*\""), greaterThan(0));
        assertThat(queryString.indexOf("\"boost\":\"2\""), greaterThan(0));
    }

    // endregion Happy path

    // region Sad path

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_params_added_then_exception_is_thrown()
            throws MandatoryParametersAreMissingException {

        WildCardQuery
            .builder()
            .build();
    }

    // endregion Sad path

}
