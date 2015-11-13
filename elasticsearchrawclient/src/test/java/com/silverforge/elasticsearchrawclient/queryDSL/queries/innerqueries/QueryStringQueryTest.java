package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.QueryTest;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.LogicOperator;

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

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = QueryTest.class)
public class QueryStringQueryTest {

    // region Happy path

    @Test
    public void when_default_field_and_query_parameters_added_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        String queryString = QueryStringQuery
            .builder()
            .defaultField("myField")
            .query("apple OR banana")
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"query_string\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"default_field\":\"myField\""), greaterThan(0));
        assertThat(queryString.indexOf("\"query\":\"apple OR banana\""), greaterThan(0));
    }

    @Test
    public void when_complicated_query_parameters_defined_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        String queryString = QueryStringQuery
            .builder()
            .fields("myName", "yourName", "herName")
            .query("apple OR banana")
            .fuzziness("4")
            .fuzzyMaxExpansions(5)
            .fuzzyPrefixLength(3.4f)
            .allowLeadingWildcard(false)
            .analyzer("myCustomAnalyzer")
            .analyzeWildcard(true)
            .autoGeneratePhraseQueries(false)
            .boost(5.123f)
            .defaultOperator(LogicOperator.AND)
            .enablePositionIncrements(false)
            .lenient(true)
            .locale("HU")
            .lowercaseExpandedTerms(true)
            .maxDeterminizedStates(6)
            .minimumShouldMatchCombination("3>56%")
            .phraseSlop(5.1f)
            .timeZone("-08:45")
            .useDisMax(true)
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"query_string\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"fields\":[\"myName\",\"yourName\",\"herName\"]"), greaterThan(0));
        assertThat(queryString.indexOf("\"query\":\"apple OR banana\""), greaterThan(0));
        assertThat(queryString.indexOf("\"fuzziness\":\"4\""), greaterThan(0));
        assertThat(queryString.indexOf("\"fuzzy_max_expansions\":\"5\""), greaterThan(0));
        assertThat(queryString.indexOf("\"fuzzy_prefix_length\":\"3.4\""), greaterThan(0));
        assertThat(queryString.indexOf("\"allow_leading_wildcard\":\"false\""), greaterThan(0));
        assertThat(queryString.indexOf("\"analyzer\":\"myCustomAnalyzer\""), greaterThan(0));
        assertThat(queryString.indexOf("\"analyze_wildcard\":\"true\""), greaterThan(0));
        assertThat(queryString.indexOf("\"auto_generate_phrase_queries\":\"false\""), greaterThan(0));
        assertThat(queryString.indexOf("\"boost\":\"5.123\""), greaterThan(0));
        assertThat(queryString.indexOf("\"default_operator\":\"and\""), greaterThan(0));
        assertThat(queryString.indexOf("\"enable_position_increments\":\"false\""), greaterThan(0));
        assertThat(queryString.indexOf("\"lenient\":\"true\""), greaterThan(0));
        assertThat(queryString.indexOf("\"locale\":\"HU\""), greaterThan(0));
        assertThat(queryString.indexOf("\"lowercase_expanded_terms\":\"true\""), greaterThan(0));
        assertThat(queryString.indexOf("\"max_determinized_states\":\"6\""), greaterThan(0));
        assertThat(queryString.indexOf("\"minimum_should_match\":\"3>56%\""), greaterThan(0));
        assertThat(queryString.indexOf("\"phrase_slop\":\"5.1\""), greaterThan(0));
        assertThat(queryString.indexOf("\"time_zone\":\"-08:45\""), greaterThan(0));
        assertThat(queryString.indexOf("\"use_dis_max\":\"true\""), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_parameters_added_then_exception_thrown()
            throws MandatoryParametersAreMissingException {

        QueryStringQuery
            .builder()
            .build();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_field_param_added_then_exception_thrown()
            throws MandatoryParametersAreMissingException {

        QueryStringQuery
            .builder()
            .query("apple")
            .build();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_query_param_added_then_exception_thrown()
            throws MandatoryParametersAreMissingException {

        QueryStringQuery
            .builder()
            .defaultField("apple")
            .build();
    }

    // endregion
}
