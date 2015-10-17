package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerqueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
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
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
    }
}
