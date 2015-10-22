package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
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
public class BoostingQueryTest {

    // region Happy path

    @Test
    public void when_query_fully_defined_then_query_generated_with_positive_negative() {
        BoostingQuery query = BoostingQuery
            .builder()
            .positive(
                MatchQuery
                    .builder()
                    .allFields()
                    .build(),
                MatchQuery
                    .builder()
                    .fieldName("name")
                    .value("Karcag")
                    .build())
            .negative(
                MatchQuery
                    .builder()
                    .build())
            .negativeBoost(ZeroToOneRangeOperator._0_6)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"boosting\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"positive\":["), greaterThan(0));
        assertThat(queryString.indexOf("\"negative\":["), greaterThan(0));

        assertThat(queryString.indexOf("\"negative_boost\":\"0.6\""), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test
    public void when_no_parameters_defined_then_empty_query_string_is_generated() {
        BoostingQuery query = BoostingQuery
            .builder()
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString, is("{\"boosting\":{}}"));
    }

    // endregion
}
