package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.LogicOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ZeroToOneRangeOperator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class CommonTermsQueryTest {

    // region Happy path

    @Test
    public void when_minimal_query_parameters_added_then_query_generated_well() {
        CommonTermsQuery query = CommonTermsQuery
            .builder()
            .field("oregapam")
            .query("alma barack citrom")
            .build();

        String queryString = query.getQueryString();
        System.out.println(queryString);

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
        assertThat(queryString, is("{\"common\":{\"oregapam\":{\"query\":\"alma barack citrom\"}}}"));
    }

    @Test
    public void when_minimumShouldMatch_defined_then_lowFreq_and_highFreq_parameters_are_ignored() {
        CommonTermsQuery query = CommonTermsQuery
            .builder()
            .field("oregapam")
            .query("alma barack citrom")
            .minimumShouldMatch(4)
            .lowFreq(3)
            .highFreq(6)
            .build();

        String queryString = query.getQueryString();
        System.out.println(queryString);

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"common\":{\"oregapam\":{"), is(true));
        assertThat(queryString.endsWith("}}}"), is(true));

        assertThat(queryString.indexOf("\"query\":\"alma barack citrom\""), greaterThan(0));
        assertThat(queryString.indexOf("\"minimum_should_match\":\"4\""), greaterThan(0));

        assertThat(queryString.indexOf("\"low_freq\""), is(-1));
        assertThat(queryString.indexOf("\"high_freq\""), is(-1));
    }

    @Test
    public void when_low_freq_and_high_freq_defined_then_minimum_should_match_is_the_group() {
        CommonTermsQuery query = CommonTermsQuery
            .builder()
            .field("oregapam")
            .query("alma barack citrom")
            .lowFreq(3)
            .highFreq(6)
            .build();

        String queryString = query.getQueryString();
        System.out.println(queryString);

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"common\":{\"oregapam\":{"), is(true));
        assertThat(queryString.endsWith("}}}"), is(true));

        assertThat(queryString.indexOf("\"query\":\"alma barack citrom\""), greaterThan(0));
        assertThat(queryString.indexOf("\"minimum_should_match\":{"), greaterThan(0));

        assertThat(queryString.indexOf("\"low_freq\":\"3\""), greaterThan(0));
        assertThat(queryString.indexOf("\"high_freq\":\"6\""), greaterThan(0));
    }

    @Test
    public void when_only_low_freq_is_defined_then_minimum_should_match_is_the_group() {
        CommonTermsQuery query = CommonTermsQuery
            .builder()
            .field("oregapam")
            .query("alma barack citrom")
            .lowFreq(3)
            .build();

        String queryString = query.getQueryString();
        System.out.println(queryString);

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"common\":{\"oregapam\":{"), is(true));
        assertThat(queryString.endsWith("}}}"), is(true));

        assertThat(queryString.indexOf("\"query\":\"alma barack citrom\""), greaterThan(0));
        assertThat(queryString.indexOf("\"minimum_should_match\":{\"low_freq\":\"3\""), greaterThan(0));
    }

    @Test
    public void when_only_high_freq_is_defined_then_minimum_should_match_is_the_group() {
        CommonTermsQuery query = CommonTermsQuery
            .builder()
            .field("oregapam")
            .query("alma barack citrom")
            .highFreq(23)
            .build();

        String queryString = query.getQueryString();
        System.out.println(queryString);

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"common\":{\"oregapam\":{"), is(true));
        assertThat(queryString.endsWith("}}}"), is(true));

        assertThat(queryString.indexOf("\"query\":\"alma barack citrom\""), greaterThan(0));
        assertThat(queryString.indexOf("\"minimum_should_match\":{\"high_freq\":\"23\""), greaterThan(0));
    }

    @Test
    public void when_all_param_is_defined_then_query_generated_well() {
        CommonTermsQuery query = CommonTermsQuery
            .builder()
            .field("oregapam")
            .query("alma barack citrom")
            .highFreqCombination("3<50%")
            .lowFreq(72)
            .highFreqOperator(LogicOperator.OR)
            .lowFreqOperator(LogicOperator.AND)
            .cutoffFrequency(ZeroToOneRangeOperator._0_2)
            .build();

        String queryString = query.getQueryString();
        System.out.println(queryString);

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"common\":{\"oregapam\":{"), is(true));
        assertThat(queryString.endsWith("}}}"), is(true));

        assertThat(queryString.indexOf("\"query\":\"alma barack citrom\""), greaterThan(0));

        assertThat(queryString.indexOf("\"minimum_should_match\":{"), greaterThan(0));
        assertThat(queryString.indexOf("\"high_freq\":\"3<50%\""), greaterThan(0));
        assertThat(queryString.indexOf("\"low_freq\":\"72\""), greaterThan(0));

        assertThat(queryString.indexOf("\"low_freq_operator\":\"and\""), greaterThan(0));
        assertThat(queryString.indexOf("\"high_freq_operator\":\"or\""), greaterThan(0));

        assertThat(queryString.indexOf("\"cutoff_frequency\":\"0.2\""), greaterThan(0));
    }

    // endregion

    //region Sad path

    @Test
    public void when_field_is_not_defined_then_all_fields_generated() {
        CommonTermsQuery query = CommonTermsQuery
            .builder()
            .build();

        String queryString = query.getQueryString();
        System.out.println(queryString);

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"common\":{\"_all\":{"), is(true));
        assertThat(queryString.endsWith("}}}"), is(true));
    }

    @Test
    public void when_query_is_not_defined_then_all_fields_generated() {
        CommonTermsQuery query = CommonTermsQuery
            .builder()
            .query(null)
            .build();

        String queryString = query.getQueryString();
        System.out.println(queryString);

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"common\":{\"_all\":{"), is(true));
        assertThat(queryString.endsWith("}}}"), is(true));

        assertThat(queryString.indexOf("\"query\":\"\""), greaterThan(0));
    }

    // endregion
}
