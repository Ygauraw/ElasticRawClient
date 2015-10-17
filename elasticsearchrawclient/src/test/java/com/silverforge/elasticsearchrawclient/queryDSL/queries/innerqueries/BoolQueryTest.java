package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.LogicOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.TieBreakerOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.Queryable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class BoolQueryTest {

    @Test
    public void when_must_defined_then_query_generated_with_must() {
        BoolQuery query = BoolQuery
            .builder()
            .must(
                MatchQuery
                    .builder()
                    .allFields()
                    .build(),
                MatchQuery
                    .builder()
                    .fieldName("name")
                    .value("Karcag")
                    .build())
            .should(
                MultiMatchQuery
                    .builder()
                    .fields("name", "population")
                    .tieBreaker(TieBreakerOperator._0_4)
                    .query("Karcag Budapest")
                    .useDisMax(false)
                    .analyzer("standard")
                    .build())
            .mustNot(
                BoolQuery
                    .builder()
                    .should(
                        MatchQuery
                            .builder()
                            .fieldName("name")
                            .query("Karcag Budapest")
                            .operator(LogicOperator.OR)
                            .analyzer("keyword")
                            .build())
                    .build())
            .minimumShouldMatch(23)
            .disableCoord(false)
            .boost(1.0F)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
    }
}
