package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ScoreModeOperator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class HasChildQueryTest {

    // region Happy path

    @Test
    public void when_minimal_requred_params_added_then_query_is_generated_well() throws MandatoryParametersAreMissingException {
        HasChildQuery query = HasChildQuery
                .builder()
                .query(MatchQuery
                        .builder()
                        .build())
                .type("value")
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"query\":{\"match\":{\"_all\":\"\"}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"type\":\"value\""), greaterThan(0));
    }

    @Test
    public void when_all_params_added_then_query_is_generated_well() throws MandatoryParametersAreMissingException {
        HasChildQuery query = HasChildQuery
                .builder()
                .query(MatchQuery
                        .builder()
                        .build())
                .type("value")
                .scoreMode(ScoreModeOperator.MIN)
                .maxChildren(2)
                .minChildren(1)
                .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"query\":{\"match\":{\"_all\":\"\"}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"type\":\"value\""), greaterThan(0));
        assertThat(queryString.indexOf("\"score_mode\":\"min\""), greaterThan(0));
        assertThat(queryString.indexOf("\"max_children\":\"2\""), greaterThan(0));
        assertThat(queryString.indexOf("\"min_children\":\"1\""), greaterThan(0));
    }

    // endregion Happy path

    // region Sad path

    // TODO : all other excepted exception tests should use this approach here and at HasParent, Ids as well
    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_params_added_then_exception_is_thrown()
            throws MandatoryParametersAreMissingException {

        HasChildQuery
                .builder()
                .build();
    }

    @Test
    public void when_no_query_added_then_exception_is_thrown()  {
        boolean thrown = false;

        try {
            HasChildQuery query = HasChildQuery
                    .builder()
                    .type("value")
                    .minChildren(1)
                    .maxChildren(2)
                    .build();
        } catch (MandatoryParametersAreMissingException e) {
            thrown = true;
        }

        assertTrue(thrown);
    }

    @Test
    public void when_no_type_added_then_exception_is_thrown()  {
        boolean thrown = false;

        try {
            HasChildQuery query = HasChildQuery
                    .builder()
                    .query(MatchQuery
                            .builder()
                            .build())
                    .minChildren(1)
                    .maxChildren(2)
                    .build();
        } catch (MandatoryParametersAreMissingException e) {
            thrown = true;
        }

        assertTrue(thrown);
    }

    // endregion Sad path

}
