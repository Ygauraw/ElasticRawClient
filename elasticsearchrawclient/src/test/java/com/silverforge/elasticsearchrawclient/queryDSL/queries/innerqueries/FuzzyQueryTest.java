package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.QueryTest;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.FuzzinessOperator;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = QueryTest.class)
public class FuzzyQueryTest {

    // region Happy path

    @Test
    public void when_all_field_name_defined_with_value_then_minimum_best_query_generated_well() {
        FuzzyQuery query = FuzzyQuery
            .builder()
            .allFields()
            .value("Karcag")
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString, is("{\"fuzzy\":{\"_all\":\"Karcag\"}}"));
    }

    @Test
    public void when_custom_fields_defined_with_value_then_minimum_best_query_generated_well() {
        FuzzyQuery query = FuzzyQuery
            .builder()
            .fieldName("name")
            .value("Karcag")
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString, is("{\"fuzzy\":{\"name\":\"Karcag\"}}"));
    }

    @Test
    public void when_all_possible_fields_defined_with_value_then_minimum_best_query_generated_well() {
        FuzzyQuery query = FuzzyQuery
            .builder()
            .fieldName("name")
            .value("Karcag")
            .boost(2.8f)
            .fuzziness(FuzzinessOperator._1)
            .prefixLength(3)
            .maxExpansions(4)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"fuzzy\":{\"name\":"), is(true));
        assertThat(queryString.indexOf("\"value\":\"Karcag\""), greaterThan(0));
        assertThat(queryString.indexOf("\"boost\":\"2.8\""), greaterThan(0));
        assertThat(queryString.indexOf("\"fuzziness\":\"1\""), greaterThan(0));
        assertThat(queryString.indexOf("\"prefix_length\":\"3\""), greaterThan(0));
        assertThat(queryString.indexOf("\"max_expansions\":\"4\""), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test
    public void when_no_fields_defined_then_query_generated_well() {

        FuzzyQuery matchQuery =
            FuzzyQuery
                .builder()
                .value("Karcag")
                .build();

        String queryString = matchQuery.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
        assertThat(queryString, is("{\"fuzzy\":{\"_all\":\"Karcag\"}}"));
    }

    @Test
    public void when_nothing_is_defined_then_most_generic_query_generated() {
        FuzzyQuery matchQuery =
            FuzzyQuery
                .builder()
                .build();

        String queryString = matchQuery.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
        assertThat(queryString, is("{\"fuzzy\":{\"_all\":\"\"}}"));
    }

    @Test
    public void when_null_parameters_set_then_most_generic_query_generated() {
        FuzzyQuery matchQuery =
            FuzzyQuery
                .builder()
                .fieldName(null)
                .value(null)
                .build();

        String queryString = matchQuery.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
        assertThat(queryString, is("{\"fuzzy\":{\"_all\":\"\"}}"));
    }

    // endregion
}
