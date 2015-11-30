package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.definition.QueryTest;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.FuzzinessOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.FuzzyRewriteOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.LogicOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.PhraseTypeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ZeroTermsQueryOperator;

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
public class MatchQueryTest {

    // region Happy path

    @Test
    public void when_String_value_added_with_field_name_then_query_generated_well() {
        MatchQuery matchQuery =
            MatchQuery
                .builder()
                .fieldName("name")
                .value("Karcag")
                .build();

        String queryString = matchQuery.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
        assertThat(queryString, is("{\"match\":{\"name\":\"Karcag\"}}"));
    }

    @Test
    public void when_String_value_added_with_all_field_name_then_query_generated_well() {
        MatchQuery matchQuery =
            MatchQuery
                .builder()
                .allFields()
                .value("Karcag")
                .build();

        String queryString = matchQuery.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
        assertThat(queryString, is("{\"match\":{\"_all\":\"Karcag\"}}"));
    }

    @Test
    public void when_all_query_parameters_added_then_query_generated_well() {
        MatchQuery matchQuery = MatchQuery
            .builder()
            .fieldName("name")
            .query("Karcag Budapest")
            .analyzer("standard")
            .fuzziness(FuzzinessOperator._0)
            .fuzzyRewrite(FuzzyRewriteOperator.TOP_TERMS_BOOST_N, (byte)3)
            .lenient(false)
            .operator(LogicOperator.AND)
            .minimumShouldMatchPercentage(12)
            .maxExpansions(2)
            .prefixLength(1)
            .type(PhraseTypeOperator.PHRASE)
            .zeroTermsQuery(ZeroTermsQueryOperator.NONE)
            .build();

        String queryString = matchQuery.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("{\"match\":{\"name\":{"), is(0));
        assertThat(queryString.indexOf("\"query\":\"Karcag Budapest\""), greaterThan(0));
        assertThat(queryString.indexOf("\"analyzer\":\"standard\""), greaterThan(0));
        assertThat(queryString.indexOf("\"fuzziness\":\"0\""), greaterThan(0));
        assertThat(queryString.indexOf("\"fuzzy_rewrite\":\"top_terms_boost_3\""), greaterThan(0));
        assertThat(queryString.indexOf("\"lenient\":\"false\""), greaterThan(0));
        assertThat(queryString.indexOf("\"operator\":\"and\""), greaterThan(0));
        assertThat(queryString.indexOf("\"minimum_should_match\":\"12%\""), greaterThan(0));
        assertThat(queryString.indexOf("\"max_expansions\":\"2\""), greaterThan(0));
        assertThat(queryString.indexOf("\"prefix_length\":\"1\""), greaterThan(0));
        assertThat(queryString.indexOf("\"type\":\"phrase\""), greaterThan(0));
        assertThat(queryString.indexOf("\"zero_terms_query\":\"none\""), greaterThan(0));

        assertThat(queryString.indexOf("\",\""), greaterThan(0));
        assertThat(queryString.indexOf("\"\""), is(-1));
        assertThat(queryString.indexOf("}}}"), greaterThan(0));
    }

    @Test
    public void when_fuzziness_defined_manually_then_query_generated_well() {
        MatchQuery matchQuery = MatchQuery
            .builder()
            .fieldName("name")
            .query("Kar")
            .fuzziness("5")
            .type(PhraseTypeOperator.PHRASE_PREFIX)
            .build();

        String queryString = matchQuery.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("{\"match\":{\"name\":{"), is(0));
        assertThat(queryString.indexOf("\"query\":\"Kar\""), greaterThan(0));
        assertThat(queryString.indexOf("\"fuzziness\":\"5\""), greaterThan(0));
        assertThat(queryString.indexOf("\"type\":\"phrase_prefix\""), greaterThan(0));

        assertThat(queryString.indexOf("\",\""), greaterThan(0));
        assertThat(queryString.indexOf("\"\""), is(-1));
        assertThat(queryString.indexOf("}}}"), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test
    public void when_no_fields_defined_then_query_generated_well() {

        MatchQuery matchQuery =
            MatchQuery
                .builder()
                .value("Karcag")
                .build();

        String queryString = matchQuery.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
        assertThat(queryString, is("{\"match\":{\"_all\":\"Karcag\"}}"));
    }

    @Test
    public void when_nothing_is_defined_then_most_generic_query_generated() {
        MatchQuery matchQuery =
            MatchQuery
                .builder()
                .build();

        String queryString = matchQuery.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
        assertThat(queryString, is("{\"match\":{\"_all\":\"\"}}"));
    }

    @Test
    public void when_null_parameters_set_then_most_generic_query_generated() {
        MatchQuery matchQuery = MatchQuery
            .builder()
            .fieldName(null)
            .value(null)
            .build();

        String queryString = matchQuery.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
        assertThat(queryString, is("{\"match\":{\"_all\":\"\"}}"));
    }

    // endregion
}
