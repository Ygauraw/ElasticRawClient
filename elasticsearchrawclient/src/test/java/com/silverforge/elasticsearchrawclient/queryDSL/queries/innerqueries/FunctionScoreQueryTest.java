package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.QueryTest;
import com.silverforge.elasticsearchrawclient.queryDSL.functions.Function;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.BoostModeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ScoreModeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.scripts.Script;


import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = QueryTest.class)
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
                        .weight(2)
                        .randomScore(2)
                        .build())
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
        assertThat(queryString.indexOf("\"query\":{\"match\":{\"_all\":\"\"}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"function\":{\"seed\":\"2\",\"weight\":\"2\"}"), greaterThan(0));
    }

    @Test
    public void when_all_the_params_added_then_query_is_generated_well() {
        Map<String, Integer> params = new HashMap<>();
        params.put("param1",1);
        params.put("param2", 2);

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
                        .scriptScore(
                                Script
                                .builder()
                                .lang("lang")
                                .params(params)
                                .build()
                        )
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
        assertThat(queryString.indexOf("\"function\":{\"script\":{\"lang\":\"lang\",\"params\":[param1:1,param2:2]}}"), greaterThan(0));
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