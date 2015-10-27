package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.StrategyOperator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class FilteredQueryTest {

    // region Happy path

    @Test
    public void simple() {

        FilteredQuery query = FilteredQuery
            .builder()
            .query(MatchQuery
                .builder()
                .build())
            .strategy(StrategyOperator.LEAP_FROG_FILTER_FIRST)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));


    }

    // endregion

    // region Sad path

    // endregion
}
