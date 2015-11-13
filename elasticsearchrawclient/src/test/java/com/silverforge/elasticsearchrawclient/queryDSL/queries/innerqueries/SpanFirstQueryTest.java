package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.QueryTest;

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
public class SpanFirstQueryTest {

    // region Happy path

    @Test
    public void when_query_defined_then_query_generated()
            throws MandatoryParametersAreMissingException {

        String queryString = SpanFirstQuery
            .builder()
            .fieldName("myPreciousField")
            .value(34)
            .end(6)
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"span_first\":{\"match\":{\"span_term\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"myPreciousField\":\"34\""), greaterThan(0));
        assertThat(queryString.indexOf("\"end\":\"6\""), greaterThan(0));
    }

    @Test
    public void when_query_defined_all_fields_then_query_generated()
            throws MandatoryParametersAreMissingException {

        String queryString = SpanFirstQuery
            .builder()
            .allFields()
            .value(34)
            .end(6)
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"span_first\":{\"match\":{\"span_term\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"_all\":\"34\""), greaterThan(0));
        assertThat(queryString.indexOf("\"end\":\"6\""), greaterThan(0));
    }

    @Test
    public void when_no_end_defined_then_query_generated()
            throws MandatoryParametersAreMissingException {

        String queryString = SpanFirstQuery
            .builder()
            .allFields()
            .value(34)
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"span_first\":{\"match\":{\"span_term\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"_all\":\"34\""), greaterThan(0));
        assertThat(queryString.indexOf("\"end\":\"3\""), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_params_defined_then_query_generated()
            throws MandatoryParametersAreMissingException {

        SpanFirstQuery
            .builder()
            .build()
            .getQueryString();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_fields_defined_then_query_generated()
            throws MandatoryParametersAreMissingException {

        SpanFirstQuery
            .builder()
            .value(true)
            .build()
            .getQueryString();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_value_defined_then_query_generated()
            throws MandatoryParametersAreMissingException {

        SpanFirstQuery
            .builder()
            .fieldName("apple")
            .build()
            .getQueryString();
    }

    // endregion
}
