package com.silverforge.elasticsearchrawclient.queryDSL.filters.innerfilters;

import com.silverforge.elasticsearchrawclient.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MatchAllFilterTest {

    @Test
    public void when_filter_defined_then_query_generated_well() {

        MatchAllFilter filter = MatchAllFilter
            .builder()
            .build();

        String filterString = filter.getQueryString();

        assertThat(filterString, notNullValue());
        assertThat(filterString, not(""));

        assertThat(filterString, is("{\"constant_score\":{\"filter\":{\"match_all\":{}}}}"));
    }

}
