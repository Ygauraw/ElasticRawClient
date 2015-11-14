package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.QueryTest;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.ScoreModeOperator;

import org.junit.Test;
import org.junit.experimental.categories.Category;
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
@Category(value = QueryTest.class)
public class HasParentQueryTest {

    // region Happy path

    @Test
    public void when_minimal_required_params_added_then_query_is_generated_well() throws MandatoryParametersAreMissingException {
        HasParentQuery query = HasParentQuery
            .builder()
            .query(MatchQuery
                .builder()
                .build())
            .parent_type("value")
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"query\":{\"match\":{\"_all\":\"\"}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"parent_type\":\"value\""), greaterThan(0));
    }

    @Test
    public void when_all_params_added_then_query_is_generated_well() throws MandatoryParametersAreMissingException {
        HasParentQuery query = HasParentQuery
            .builder()
            .query(MatchQuery
                .builder()
                .build())
            .parent_type("value")
            .scoreMode(ScoreModeOperator.MIN)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.indexOf("\"query\":{\"match\":{\"_all\":\"\"}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"parent_type\":\"value\""), greaterThan(0));
        assertThat(queryString.indexOf("\"score_mode\":\"min\""), greaterThan(0));
    }

    // endregion Happy path

    // region Sad path

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_params_added_then_exception_is_thrown() throws MandatoryParametersAreMissingException {
        HasParentQuery query = HasParentQuery
            .builder()
            .build();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_query_added_then_exception_is_thrown() throws MandatoryParametersAreMissingException {
        HasParentQuery query = HasParentQuery
            .builder()
            .parent_type("value")
            .build();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_parent_type_added_then_exception_is_thrown() throws MandatoryParametersAreMissingException {
        HasParentQuery query = HasParentQuery
            .builder()
            .query(MatchQuery
                .builder()
                .build())
            .build();
    }

    // endregion Sad path

}
