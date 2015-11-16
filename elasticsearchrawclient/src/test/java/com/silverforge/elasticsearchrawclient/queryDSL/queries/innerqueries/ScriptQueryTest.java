package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.QueryTest;
import com.silverforge.elasticsearchrawclient.queryDSL.scripts.Script;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = QueryTest.class)
public class ScriptQueryTest {

    // region Happy path

    @Test
    public void when_all_field_name_defined_with_value_then_minimum_best_query_generated_well() {
        Map<String, Integer> params = new HashMap<>();
        params.put("param1", 1);
        params.put("param2", 2);
        ScriptQuery query = ScriptQuery
            .builder()
            .script(Script
                    .builder()
                    .inline("inline_script_value")
                    .lang("lang")
                    .params(params)
                    .build())
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"script\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"inline\":\"inline_script_value\""), greaterThan(0));
        assertThat(queryString.indexOf("\"lang\":\"lang\""), greaterThan(0));
        assertThat(queryString.indexOf("\"params\":[param1:1,param2:2]"), greaterThan(0));

    }

    @Test
    public void when_only_script_added_then_query_is_generated_well() {
        ScriptQuery query = ScriptQuery
            .builder()
            .script(Script
                    .builder()
                    .inline("inline_script_value")
                    .build())
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"script\":{"), Matchers.is(true));
        assertThat(queryString.endsWith("}}"), Matchers.is(true));

        assertThat(queryString.indexOf("\"inline\":\"inline_script_value\""), greaterThan(0));

    }

    // endregion Happy path

    // region Sad path

    @Test
    public void when_no_params_added_then_query_is_generated_well() {
        ScriptQuery query = ScriptQuery
            .builder()
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"script\":{"), Matchers.is(true));
        assertThat(queryString.endsWith("}}"), Matchers.is(true));

    }

    // endregion Sad path

}
