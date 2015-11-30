package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.definition.QueryTest;

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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = QueryTest.class)
public class TemplateQueryTest {

    // region Happy path

    @Test
    public void when_minimal_required_params_added_then_query_is_generated_well() throws MandatoryParametersAreMissingException {

        MatchQuery matchQuery = mock(MatchQuery.class);
        when(matchQuery.getQueryString()).thenReturn("{\"match\":{\"_all\":\"Karcag\"}}");

        TemplateQuery query = TemplateQuery
                .builder()
                .inline(matchQuery)
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"template\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"template\":{\"inline\":{"), greaterThan(0));
        assertThat(queryString.indexOf("\"inline\":{\"match\":{\"_all\":\"Karcag\"}}"), greaterThan(0));

    }

    @Test
    public void when_all_params_added_then_query_is_generated_well() throws MandatoryParametersAreMissingException {

        MatchQuery matchQuery = mock(MatchQuery.class);
        when(matchQuery.getQueryString()).thenReturn("{\"match\":{\"_all\":\"Karcag\"}}");

        Map<String, String> params = new HashMap<>();
        params.put("param1","param");
        params.put("param2","param");

        TemplateQuery query = TemplateQuery
                .builder()
                .inline(matchQuery)
                .params(params)
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"template\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"template\":{\"inline\":{"), greaterThan(0));
        assertThat(queryString.indexOf("\"inline\":{\"match\":{\"_all\":\"Karcag\"}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"params\":{\"param1\":\"param\",\"param2\":\"param\"}"), greaterThan(0));

    }

    @Test
    public void when_template_id_added_then_query_is_generated_well() throws MandatoryParametersAreMissingException {

        Map<String, String> params = new HashMap<>();
        params.put("param1","param");
        params.put("param2", "param");

        TemplateQuery query = TemplateQuery
                .builder()
                .params(params)
                .id("id")
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"template\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"template\":{"), greaterThan(0));
        assertThat(queryString.indexOf("\"id\":\"id\""), greaterThan(0));
        assertThat(queryString.indexOf("\"params\":{\"param1\":\"param\",\"param2\":\"param\"}"), greaterThan(0));

    }

    @Test
    public void when_file_added_then_query_is_generated_well() throws MandatoryParametersAreMissingException {

        TemplateQuery query = TemplateQuery
                .builder()
                .file("file")
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"template\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"template\":{"), greaterThan(0));
        assertThat(queryString.indexOf("\"file\":\"file\""), greaterThan(0));

    }

    @Test
    public void when_text_inline_added_then_query_is_generated_well() throws MandatoryParametersAreMissingException {

        TemplateQuery query = TemplateQuery
                .builder()
                .inline("{\"match\":{\"_all\":\"Karcag\"}}")
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"template\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"template\":{\"inline\":{"), greaterThan(0));
        assertThat(queryString.indexOf("\"inline\":{\"match\":{\"_all\":\"Karcag\"}}"), greaterThan(0));

    }

    // endregion Happy path

    // region Sad path

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_query_added_then_exception_is_thrown() throws MandatoryParametersAreMissingException {

        Map<String, String> params = new HashMap<>();
        params.put("param1","param");
        params.put("param2", "param");

        TemplateQuery query = TemplateQuery
                .builder()
                .params(params)
                .build();

    }

    // endregion Sad path

}
