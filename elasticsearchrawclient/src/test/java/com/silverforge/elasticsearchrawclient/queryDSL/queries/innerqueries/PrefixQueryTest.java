package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.definition.QueryTest;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = QueryTest.class)
public class PrefixQueryTest {

    // region Happy path

    @Test
    public void when_all_parameters_defined_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        String queryString = PrefixQuery
            .builder()
            .fieldName("myPreciousField")
            .value("Apfel ist gut")
            .boost(3)
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"prefix\":{\"myPreciousField\":{"), is(true));
        assertThat(queryString.endsWith("}}}"), is(true));

        assertThat(queryString.indexOf("\"value\":\"Apfel ist gut\""), greaterThan(0));
        assertThat(queryString.indexOf("\"boost\":\"3\""), greaterThan(0));
    }

    @Test
    public void when_all_fields_parameters_defined_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        String queryString = PrefixQuery
            .builder()
            .allFields()
            .value("Apfel ist gut")
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"prefix\":{\"_all\":{"), is(true));
        assertThat(queryString.endsWith("}}}"), is(true));

        assertThat(queryString.indexOf("\"value\":\"Apfel ist gut\""), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_fields_defined_then_exception_thrown()
        throws MandatoryParametersAreMissingException {

        PrefixQuery
            .builder()
            .build()
            .getQueryString();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_value_defined_then_exception_thrown()
        throws MandatoryParametersAreMissingException {

        PrefixQuery
            .builder()
            .allFields()
            .build()
            .getQueryString();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_field_defined_then_exception_thrown()
        throws MandatoryParametersAreMissingException {

        PrefixQuery
            .builder()
            .value(53)
            .build()
            .getQueryString();
    }

    // endregion
}
