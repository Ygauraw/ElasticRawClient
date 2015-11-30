package com.silverforge.elasticsearchrawclient.queryDSL.queries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.SortOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries.MatchQuery;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.sorting.GeoDistanceSorting;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.sorting.ScriptBasedSorting;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.sorting.Sorting;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = com.silverforge.elasticsearchrawclient.queryDSL.definition.QueryTest.class)
public class QueryTest {

    // region Happy path

    @Test
    public void when_inner_query_added_then_query_generated_well() {

        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        Query query = Query
            .builder()
            .query(matchAllQueryable)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));
        assertThat(queryString, is("{\"query\":{\"match_all\":{}}}"));
    }

    @Test
    public void when_inner_query_and_from_and_size_added_then_query_generated_well() {

        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        Query query = Query
            .builder()
            .from(20)
            .size(100)
            .query(matchAllQueryable)
            .build();

        String queryString = query.getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{"), is(true));
        assertThat(queryString.endsWith("}"), is(true));

        assertThat(queryString.indexOf("\"query\":{\"match_all\":{}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"from\":\"20\""), greaterThan(0));
        assertThat(queryString.indexOf("\"size\":\"100\""), greaterThan(0));
    }

    @Test
    public void when_sorting_is_added_then_query_generated_well()
            throws MandatoryParametersAreMissingException {
        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        String queryString = Query
            .builder()
            .query(matchAllQueryable)
            .sort(
                Sorting
                    .builder()
                    .fieldName("name")
                    .order(SortOperator.DESC)
                    .build())
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{"), is(true));
        assertThat(queryString.endsWith("}"), is(true));

        assertThat(queryString.indexOf("\"query\":{\"match_all\":{}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"sort\":[{\"name\":{\"order\":\"desc\"}}]"), greaterThan(0));
    }

    @Test
    public void when_sortings_are_added_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        ScriptBasedSorting scriptBasedSorting = mock(ScriptBasedSorting.class);
        when(scriptBasedSorting.getSortableQuery()).thenReturn("{\"_script\":{\"type\":\"number\",\"script\":{\"inline\":\"doc['field_name'].value*factor\",\"params\":{\"factor\":\"1.1\"}},\"order\":\"asc\"}}");

        GeoDistanceSorting geoDistanceSorting = mock(GeoDistanceSorting.class);
        when(geoDistanceSorting.getSortableQuery()).thenReturn("{\"_geo_distance\":{\"pin.location\":[-70, 40],\"order\":\"asc\",\"unit\":\"km\"}}");

        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        String queryString = Query
            .builder()
            .query(matchAllQueryable)
            .trackScores(false)
            .sort(
                scriptBasedSorting,
                geoDistanceSorting,
                Sorting
                    .builder()
                    .fieldName("name")
                    .order(SortOperator.DESC)
                    .build())
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{"), is(true));
        assertThat(queryString.endsWith("}"), is(true));

        assertThat(queryString.indexOf("\"track_scores\":\"false\""), greaterThan(0));
        assertThat(queryString.indexOf("\"query\":{\"match_all\":{}}"), greaterThan(0));
        assertThat(queryString.indexOf("\"sort\":[{"), greaterThan(0));
        assertThat(queryString.indexOf("}]"), greaterThan(0));

        assertThat(queryString.indexOf("{\"_script\":{\"type\":\"number\",\"script\":{\"inline\":\"doc['field_name'].value*factor\",\"params\":{\"factor\":\"1.1\"}},\"order\":\"asc\"}}"), greaterThan(0));
        assertThat(queryString.indexOf("{\"_geo_distance\":{\"pin.location\":[-70, 40],\"order\":\"asc\",\"unit\":\"km\"}}"), greaterThan(0));
        assertThat(queryString.indexOf("{\"name\":{\"order\":\"desc\"}}"), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test
    public void when_no_parameters_added_then_query_generated_well()
            throws MandatoryParametersAreMissingException {
        String queryString = Query
            .builder()
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString, is("{}"));
    }

    // endregion
}
