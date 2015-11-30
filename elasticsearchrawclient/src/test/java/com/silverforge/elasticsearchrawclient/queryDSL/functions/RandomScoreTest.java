package com.silverforge.elasticsearchrawclient.queryDSL.functions;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.definition.FunctionTest;
import com.silverforge.elasticsearchrawclient.definition.Queryable;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = FunctionTest.class)
public class RandomScoreTest {

    // region Happy path

    @Test
    public void when_seed_defined_then_function_generated_well() {
        String functionString = RandomScore
            .builder()
            .seed(78)
            .build()
            .getFunctionString();

        assertThat(functionString, notNullValue());
        assertThat(functionString, not(""));

        assertThat(functionString, is("{\"random_score\":{\"seed\":\"78\"}}"));
    }

    @Test
    public void when_seed_and_filter_defined_then_function_generated_well() {
        Queryable queryable = mock(Queryable.class);
        when(queryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        String functionString = RandomScore
            .builder()
            .seed(78)
            .filter(queryable)
            .build()
            .getFunctionString();

        assertThat(functionString, notNullValue());
        assertThat(functionString, not(""));

        assertThat(functionString.startsWith("{"), is(true));
        assertThat(functionString.endsWith("}"), is(true));

        assertThat(functionString.indexOf("\"random_score\":{\"seed\":\"78\"}"), greaterThan(0));
        assertThat(functionString.indexOf("\"filter\":{\"match_all\":{}}"), greaterThan(0));
    }

    @Test
    public void when_no_params_defined_then_empty_function_generated_well() {
        String functionString = RandomScore
            .builder()
            .build()
            .getFunctionString();

        assertThat(functionString, notNullValue());
        assertThat(functionString, not(""));

        assertThat(functionString, is("{\"random_score\":{}}"));
    }

    // endregion

    // region Sad path

    // endregion
}
