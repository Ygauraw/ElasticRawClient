package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Functionable;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.QueryTest;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.BoostModeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ScoreModeOperator;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = QueryTest.class)
public class FunctionScoreQueryTest {

    // region Happy path

    @Test
    public void when_minimal_required_params_added_then_query_is_generated_well() {
        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        FunctionScoreQuery query = FunctionScoreQuery
            .builder()
            .query(matchAllQueryable)
            .boost(89)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"function_score\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"query\":{\"match_all\":{}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"boost\":\"89\""), greaterThan(0));
    }

    @Test
    public void when_function_added_then_query_is_generated_well() {
        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        Functionable weightFunctionable = mock(Functionable.class);
        when(weightFunctionable.getFunctionString()).thenReturn("{\"weight\":\"54\"}");

        FunctionScoreQuery query = FunctionScoreQuery
            .builder()
            .query(matchAllQueryable)
            .functions(weightFunctionable)
            .boost(89)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"function_score\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"query\":{\"match_all\":{}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"boost\":\"89\""), greaterThan(0));
        assertThat(queryString.indexOf("\"functions\":[{\"weight\":\"54\"}]"), greaterThan(0));
    }

    @Test
    public void when_multiple_function_param_added_then_query_is_generated_well() {
        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        Functionable weightFunctionable = mock(Functionable.class);
        when(weightFunctionable.getFunctionString()).thenReturn("{\"weight\":\"54\"}");

        Functionable randomScoreFunctionable = mock(Functionable.class);
        when(randomScoreFunctionable.getFunctionString()).thenReturn("{\"random_score\":{\"seed\":\"44\"}}");

        FunctionScoreQuery query = FunctionScoreQuery
            .builder()
            .query(matchAllQueryable)
            .functions(weightFunctionable, randomScoreFunctionable)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
        assertThat(queryString.indexOf("\"query\":{\"match_all\":{}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"functions\":[{"), greaterThan(0));

        assertThat(queryString.indexOf("{\"weight\":\"54\"}"), greaterThan(0));
        assertThat(queryString.indexOf("{\"random_score\":{\"seed\":\"44\"}}"), greaterThan(0));
    }

    @Test
    public void when_all_the_params_added_then_query_is_generated_well() {
        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        Functionable weightFunctionable = mock(Functionable.class);
        when(weightFunctionable.getFunctionString()).thenReturn("{\"weight\":\"54\"}");

        Map<String, Integer> params = new HashMap<>();
        params.put("param1",1);
        params.put("param2", 2);

        FunctionScoreQuery query = FunctionScoreQuery
                .builder()
                .query(matchAllQueryable)
                .boost(8.9f)
                .boostMode(BoostModeOperator.MULTIPLY)
                .scoreMode(ScoreModeOperator.MULTIPLY)
                .minScore(67)
                .maxBoost(5.123f)
                .functions(weightFunctionable)
                .build();

        String queryString = query.getQueryString();
        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"query\":{\"match_all\":{}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"boost\":\"8.9\""), greaterThan(0));
        assertThat(queryString.indexOf("\"boost_mode\":\"multiply\""), greaterThan(0));
        assertThat(queryString.indexOf("\"score_mode\":\"multiply\""), greaterThan(0));
        assertThat(queryString.indexOf("\"min_score\":\"67\""), greaterThan(0));
        assertThat(queryString.indexOf("\"max_boost\":\"5.123\""), greaterThan(0));
        assertThat(queryString.indexOf("\"functions\":[{\"weight\":\"54\"}]"), greaterThan(0));
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
            .functions(null)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"boost\":\"89\""), greaterThan(0));
        assertThat(queryString.indexOf("\"boost_mode\":\"multiply\""), greaterThan(0));
        assertThat(queryString.indexOf("\"functions\""), lessThan(0));
        assertThat(queryString.indexOf("\"function\""), lessThan(0));
    }

    @Test
    public void when_function_param_is_null_then_query_is_generated_well() {
        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        FunctionScoreQuery query = FunctionScoreQuery
            .builder()
            .query(matchAllQueryable)
            .boost(89)
            .boostMode(BoostModeOperator.MULTIPLY)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"query\":{\"match_all\":{}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"boost\":\"89\""), greaterThan(0));
        assertThat(queryString.indexOf("\"boost_mode\":\"multiply\""), greaterThan(0));
    }

    // endregion
}