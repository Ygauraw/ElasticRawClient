package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

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
            .lowFreq(3)
            .highFreq(5)
//            .minimumShouldMatch(4)
            .lowFreq(3)
            .highFreq(6)
            .cutoffFrequency(ZeroToOneRangeOperator._0_0)
            .lowFreqOperator(LogicOperator.OR)
            .highFreqOperator(LogicOperator.AND)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
    }

    // endregion

    //region Sad path

    // endregion
}
