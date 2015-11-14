package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.QueryTest;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.TimeZoneOperator;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = QueryTest.class)
public class RangeQueryTest {

    // region Happy path

    @Test
    public void when_minimal_required_params_added_then_query_is_generated_well()
            throws MandatoryParametersAreMissingException {

        RangeQuery query = RangeQuery
            .builder()
            .fieldName("name")
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"range\":{\"name\":\"\"}"), greaterThan(0));
    }

    @Test
    public void when_all_field_name_defined_with_value_then_minimum_best_query_generated_well()
            throws MandatoryParametersAreMissingException {

        RangeQuery query = RangeQuery
            .builder()
            .fieldName("name")
            .gte(10)
            .gt(9)
            .lte(20)
            .lt(21)
            .boost(2.0f)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        // TODO : tests should check if the query starts with curly brace and the appropriate keyword(s)
        // it would be also nice if the tests checks if the curly braces at the end of the query
        // please modify the tests in the light of this todo
//        assertThat(queryString.indexOf("\"range\":{\"name\":"), greaterThan(0));
        assertThat(queryString.startsWith("{\"range\":{\"name\":"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"gte\":\"10\""), greaterThan(0));
        assertThat(queryString.indexOf("\"gt\":\"9\""), greaterThan(0));
        assertThat(queryString.indexOf("\"lte\":\"20\""), greaterThan(0));
        assertThat(queryString.indexOf("\"lt\":\"21\""), greaterThan(0));
        assertThat(queryString.indexOf("\"boost\":\"2.0\""), greaterThan(0));
    }

    @Test
    public void when_date_used_for_range_values_then_minimum_best_query_generated_well()
            throws ParseException, MandatoryParametersAreMissingException {

        String gte = "2012-01-02";
        String gt = "2012-01-01";
        String lte = "2012-12-12";
        String lt = "2012-12-13";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        RangeQuery query = RangeQuery
            .builder()
            .fieldName("name")
            .gte(sdf.parse(gte),"yyyy-MM-dd")
            .gt(sdf.parse(gt),"yyyy-MM-dd")
            .lte(sdf.parse(lte),"yyyy-MM-dd")
            .lt(sdf.parse(lt),"yyyy-MM-dd")
            .format("dd/MM/yyyy")
            .timeZone(TimeZoneOperator.UTC)
            .boost(2.0f)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"range\":{\"name\":"), greaterThan(0));
        assertThat(queryString.indexOf("\"gte\":\"2012-01-02\""), greaterThan(0));
        assertThat(queryString.indexOf("\"gt\":\"2012-01-01\""), greaterThan(0));
        assertThat(queryString.indexOf("\"lte\":\"2012-12-12\""), greaterThan(0));
        assertThat(queryString.indexOf("\"lt\":\"2012-12-13\""), greaterThan(0));
        assertThat(queryString.indexOf("\"format\":\"dd/MM/yyyy\""), greaterThan(0));
        assertThat(queryString.indexOf("\"time_zone\":\"+00:00\""), greaterThan(0));
        assertThat(queryString.indexOf("\"boost\":\"2.0\""), greaterThan(0));
    }

    // endregion Happy path

    // region Sad path

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_params_added_then_exception_is_thrown()
            throws MandatoryParametersAreMissingException {

        RangeQuery query = RangeQuery
            .builder()
            .build();
    }

    @Test
    public void when_no_timezone_provided_then_query_is_generated_well()
            throws ParseException, MandatoryParametersAreMissingException {

        String gte = "2012-01-02";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        RangeQuery query = RangeQuery
            .builder()
            .fieldName("name")
            .gte(sdf.parse(gte), "yyyy-MM-dd")
            .format("dd/MM/yyyy")
            .boost(2.0f)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
        assertThat(queryString.indexOf("\"range\":{\"name\":"), greaterThan(0));
        assertThat(queryString.indexOf("\"gte\":\"2012-01-02\""), greaterThan(0));
        assertThat(queryString.indexOf("\"format\":\"dd/MM/yyyy\""), greaterThan(0));
        assertThat(queryString.indexOf("\"boost\":\"2.0\""), greaterThan(0));
    }

    @Test
    public void when_no_format_provided_then_query_is_generated_well()
            throws ParseException, MandatoryParametersAreMissingException {

        String gte = "2012-01-02";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        RangeQuery query = RangeQuery
            .builder()
            .fieldName("name")
            .gte(sdf.parse(gte), "yyyy-MM-dd")
            .boost(2)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
        assertThat(queryString.indexOf("\"range\":{\"name\":"), greaterThan(0));
        assertThat(queryString.indexOf("\"gte\":\"2012-01-02\""), greaterThan(0));
        assertThat(queryString.indexOf("\"boost\":\"2\""), greaterThan(0));
    }

    // endregion Sad path
}
