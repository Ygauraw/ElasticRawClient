package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.definition.QueryTest;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.LogicOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.SimpleFlagOperator;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Locale;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = QueryTest.class)
public class SimpleQueryStringQueryTest {

    // region Happy path

    @Test
    public void when_minimal_required_params_added_then_query_is_generated_well()
            throws MandatoryParametersAreMissingException {

        SimpleQueryStringQuery query = SimpleQueryStringQuery
            .builder()
            .query("\\\"fried eggs\\\" +(eggplant | potato) -frittata")
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"simple_query_string\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"query\":\"\"fried eggs\" +(eggplant | potato) -frittata\""), greaterThan(0));
    }

    @Test
    public void when_all_params_added_then_query_is_generated_well()
            throws MandatoryParametersAreMissingException {

        SimpleQueryStringQuery query = SimpleQueryStringQuery
            .builder()
            .query("\"fried eggs\" +(eggplant | potato) -frittata")
            .fields("body^5", "_all")
            .default_operator(LogicOperator.AND)
            .analyzer("analyzer value")
            .flags(SimpleFlagOperator.AND, SimpleFlagOperator.OR, SimpleFlagOperator.FUZZY)
            .lowercase_expanded_terms(true)
            .analyze_wildcard(false)
            .locale(Locale.ENGLISH)
            .lenient(true)
            .minimum_should_match("20%")
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"simple_query_string\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"query\":\"\"fried eggs\" +(eggplant | potato) -frittata\""), greaterThan(0));
        assertThat(queryString.indexOf("\"fields\":[\"body^5\",\"_all\"]"), greaterThan(0));
        assertThat(queryString.indexOf("\"default_operator\":\"and\""), greaterThan(0));
        assertThat(queryString.indexOf("\"analyzer\":\"analyzer value\""), greaterThan(0));
        assertThat(queryString.indexOf("\"flags\":\"AND|OR|FUZZY\""), greaterThan(0));
        assertThat(queryString.indexOf("\"lowercase_expanded_terms\":\"true\""), greaterThan(0));
        assertThat(queryString.indexOf("\"analyze_wildcard\":\"false\""), greaterThan(0));
        assertThat(queryString.indexOf("\"locale\":\"en\""), greaterThan(0));
        assertThat(queryString.indexOf("\"lenient\":\"true\""), greaterThan(0));
        assertThat(queryString.indexOf("\"minimum_should_match\":\"20%\""), greaterThan(0));
    }

    // endregion Happy path

    // region Sad path

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_params_added_then_exception_is_thrown()
            throws MandatoryParametersAreMissingException {

        SimpleQueryStringQuery query = SimpleQueryStringQuery
            .builder()
            .build();
    }

    @Test
    public void when_empty_query_added_then_query_is_generated_well()
            throws MandatoryParametersAreMissingException {

        SimpleQueryStringQuery query = SimpleQueryStringQuery
            .builder()
            .query("\"\"")
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"simple_query_string\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"query\":\"\""), greaterThan(0));
    }

    // endregion Sad path

}
