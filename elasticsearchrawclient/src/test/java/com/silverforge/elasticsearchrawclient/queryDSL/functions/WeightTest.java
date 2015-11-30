package com.silverforge.elasticsearchrawclient.queryDSL.functions;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.definition.FunctionTest;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.MatchAllQuery;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = FunctionTest.class)
public class WeightTest {

    // region Happy path

    @Test
    public void when_weight_value_defined_then_function_generated_well() {
        String functionString = Weight
            .builder()
            .weight(4)
            .build()
            .getFunctionString();

        assertThat(functionString, notNullValue());
        assertThat(functionString, not(""));

        assertThat(functionString, is("{\"weight\":\"4\"}"));
    }

    @Test
    public void when_filter_defined_with_weight_then_function_generated_well() {
        String functionString = Weight
            .builder()
            .filter(MatchAllQuery.builder().build())
            .weight(4)
            .build()
            .getFunctionString();

        assertThat(functionString, notNullValue());
        assertThat(functionString, not(""));

        assertThat(functionString.startsWith("{"), is(true));
        assertThat(functionString.endsWith("}"), is(true));

        assertThat(functionString.indexOf("\"weight\":\"4\""), greaterThan(0));
        assertThat(functionString.indexOf("\"filter\":{\"match_all\":{}}"), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test
    public void no_parameter_defined_then_empty_function_generated() {
        String functionString = Weight
            .builder()
            .build()
            .getFunctionString();

        assertThat(functionString, notNullValue());
        assertThat(functionString, not(""));
        assertThat(functionString, is("{}"));
    }

    // endregion
}
