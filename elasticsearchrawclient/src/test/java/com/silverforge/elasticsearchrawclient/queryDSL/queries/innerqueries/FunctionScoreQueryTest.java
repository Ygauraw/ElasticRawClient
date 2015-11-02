package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.queryDSL.functions.Function;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.BoostModeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ScoreModeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.FunctionScoreQuery;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries.MatchQuery;


import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class FunctionScoreQueryTest {

    // region Happy path

    @Test
    public void when_minimal_requred_params_added_then_query_is_generated_well() {
        FunctionScoreQuery query = FunctionScoreQuery
                .builder()
                .query(MatchQuery
                        .builder()
                        .build())
                .boost(89)
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"query\":{\"match\":{\"_all\":\"\"}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"boost\":\"89\""), greaterThan(0));
    }

    @Test
    public void when_function_param_added_then_query_is_generated_well() {
        FunctionScoreQuery query = FunctionScoreQuery
                .builder()
                .query(MatchQuery
                        .builder()
                        .build())
                .function(Function
                        .builder()
                        .build())
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
        assertThat(queryString.indexOf("\"query\":{\"match\":{\"_all\":\"\"}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"function\""), greaterThan(0));
    }

    @Test
    public void when_all_the_params_added_then_query_is_generated_well() {
        FunctionScoreQuery query = FunctionScoreQuery
                .builder()
                .query(MatchQuery
                        .builder()
                        .build())
                .boost(8.9f)
                .boostMode(BoostModeOperator.MULTIPLY)
                .scoreMode(ScoreModeOperator.MULTIPLY)
                .minScore(67)
                .maxBoost(5.123f)
                .function(Function
                        .builder()
                        .build())
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"query\":{\"match\":{\"_all\":\"\"}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"boost\":\"8.9\""), greaterThan(0));
        assertThat(queryString.indexOf("\"boost_mode\":\"multiply\""), greaterThan(0));
        assertThat(queryString.indexOf("\"score_mode\":\"multiply\""), greaterThan(0));
        assertThat(queryString.indexOf("\"min_score\":\"67\""), greaterThan(0));
        assertThat(queryString.indexOf("\"max_boost\":\"5.123\""), greaterThan(0));
        assertThat(queryString.indexOf("\"function\""), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test
    public void when_no_params_added_then_minimal_query_is_generated_well() {
        FunctionScoreQuery query = FunctionScoreQuery
                .builder()
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
    }

    @Test
    public void when_query_param_is_null_then_query_is_generated_well() {
        FunctionScoreQuery query = FunctionScoreQuery
                .builder()
                .boost(89)
                .boostMode(BoostModeOperator.MULTIPLY)
                .function(Function
                        .builder()
                        .build())
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"boost\":\"89\""), greaterThan(0));
        assertThat(queryString.indexOf("\"boost_mode\":\"multiply\""), greaterThan(0));
        assertThat(queryString.indexOf("\"function\""), greaterThan(0));
    }

    @Test
    public void when_function_param_is_null_then_query_is_generated_well() {
        FunctionScoreQuery query = FunctionScoreQuery
                .builder()
                .query(MatchQuery
                        .builder()
                        .build())
                .boost(89)
                .boostMode(BoostModeOperator.MULTIPLY)
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"query\":{\"match\":{\"_all\":\"\"}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"boost\":\"89\""), greaterThan(0));
        assertThat(queryString.indexOf("\"boost_mode\":\"multiply\""), greaterThan(0));
    }

    // endregion

}