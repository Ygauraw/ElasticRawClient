package com.silverforge.elasticsearchrawclient.queryDSL.functions;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.FunctionTest;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ModifierOperator;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = FunctionTest.class)
public class FieldValueFactorTest {

    // region Happy path

    @Test
    public void when_all_parameters_added_then_function_generated_well() {
        Queryable queryable = mock(Queryable.class);
        when(queryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        String functionString = FieldValueFactor
            .builder()
            .filter(queryable)
            .field("name.first")
            .factor(5.6f)
            .modifier(ModifierOperator.SQRT)
            .missing(3)
            .build()
            .getFunctionString();

        assertThat(functionString, notNullValue());
        assertThat(functionString, not(""));

        assertThat(functionString.startsWith("{"), is(true));
        assertThat(functionString.endsWith("}"), is(true));

        assertThat(functionString.indexOf("\"filter\":{\"match_all\":{}}"), greaterThan(0));
        assertThat(functionString.indexOf("\"field_value_factor\":{"), greaterThan(0));
        assertThat(functionString.indexOf("\"field\":\"name.first\""), greaterThan(0));
        assertThat(functionString.indexOf("\"factor\":\"5.6\""), greaterThan(0));
        assertThat(functionString.indexOf("\"modifier\":\"sqrt\""), greaterThan(0));
        assertThat(functionString.indexOf("\"missing\":\"3\""), greaterThan(0));
    }

    @Test
    public void when_no_filter_added_then_function_generated_well() {
        String functionString = FieldValueFactor
            .builder()
            .field("name.first")
            .factor(5.6f)
            .modifier(ModifierOperator.SQRT)
            .missing(3)
            .build()
            .getFunctionString();

        assertThat(functionString, notNullValue());
        assertThat(functionString, not(""));

        assertThat(functionString.startsWith("{"), is(true));
        assertThat(functionString.endsWith("}"), is(true));

        assertThat(functionString.indexOf("\"field_value_factor\":{"), greaterThan(0));
        assertThat(functionString.indexOf("\"field\":\"name.first\""), greaterThan(0));
        assertThat(functionString.indexOf("\"factor\":\"5.6\""), greaterThan(0));
        assertThat(functionString.indexOf("\"modifier\":\"sqrt\""), greaterThan(0));
        assertThat(functionString.indexOf("\"missing\":\"3\""), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test
    public void when_no_params_added_then_minimal_function_generated_well() {
        String functionString = FieldValueFactor
            .builder()
            .build()
            .getFunctionString();

        assertThat(functionString, notNullValue());
        assertThat(functionString, not(""));

        assertThat(functionString, is("{\"field_value_factor\":{}}"));
    }

    // endregion
}
