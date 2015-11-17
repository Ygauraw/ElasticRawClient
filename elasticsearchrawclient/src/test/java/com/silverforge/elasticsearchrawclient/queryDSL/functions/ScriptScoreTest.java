package com.silverforge.elasticsearchrawclient.queryDSL.functions;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.FunctionTest;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Scriptable;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = FunctionTest.class)
public class ScriptScoreTest {

    // region Happy path

    @Test
    public void when_script_defined_then_function_generated_well() {
        Scriptable scriptable = mock(Scriptable.class);
        when(scriptable.getQueryString()).thenReturn("{\"script\":\"_score*doc['my_numeric_field'].value\"}");

        String functionString = ScriptScore
            .builder()
            .script(scriptable)
            .build()
            .getFunctionString();

        assertThat(functionString, notNullValue());
        assertThat(functionString, not(""));

        assertThat(functionString.startsWith("{\"script_score\":{\"script\":\"_score*doc['my_numeric_field'].value\"}}"), is(true));
    }

    @Test
    public void when_script_and_filter_defined_then_function_generated_well() {
        Scriptable scriptable = mock(Scriptable.class);
        when(scriptable.getQueryString()).thenReturn("{\"script\":\"_score*doc['my_numeric_field'].value\"}");

        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        String functionString = ScriptScore
            .builder()
            .script(scriptable)
            .filter(matchAllQueryable)
            .build()
            .getFunctionString();

        assertThat(functionString, notNullValue());
        assertThat(functionString, not(""));

        assertThat(functionString.startsWith("{"), is(true));
        assertThat(functionString.endsWith("}"), is(true));

        assertThat(functionString.indexOf("\"filter\":{\"match_all\":{}}"), greaterThan(0));
        assertThat(functionString.indexOf("\"script_score\":{\"script\":\"_score*doc['my_numeric_field'].value\"}"), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test
    public void when_no_param_defined_then_function_generated_well() {

        String functionString = ScriptScore
            .builder()
            .build()
            .getFunctionString();

        assertThat(functionString, notNullValue());
        assertThat(functionString, not(""));

        assertThat(functionString, is("{}"));
    }

    // endregion
}
