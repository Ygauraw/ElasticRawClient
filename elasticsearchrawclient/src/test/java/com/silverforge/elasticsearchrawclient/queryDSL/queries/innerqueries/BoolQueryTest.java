package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.LogicOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ZeroToOneRangeOperator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class BoolQueryTest {

    // region Happy path

    @Test
    public void when_query_fully_defined_then_query_generated_with_must_should_must_not() {
        BoolQuery query = BoolQuery
            .builder()
            .must(
                MatchQuery
                    .builder()
                    .allFields()
                    .build(),
                MatchQuery
                    .builder()
                    .fieldName("name")
                    .value("Karcag")
                    .build())
            .should(
                MultiMatchQuery
                    .builder()
                    .fields("name", "population")
                    .tieBreaker(ZeroToOneRangeOperator._0_4)
                    .query("Karcag Budapest")
                    .useDisMax(false)
                    .analyzer("standard")
                    .build())
            .mustNot(
                BoolQuery
                    .builder()
                    .should(
                        MatchQuery
                            .builder()
                            .fieldName("name")
                            .query("Karcag Budapest")
                            .operator(LogicOperator.OR)
                            .analyzer("keyword")
                            .build())
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
