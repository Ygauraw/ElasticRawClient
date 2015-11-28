package com.silverforge.elasticsearchrawclient.queryDSL.queries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.MissingOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.SortModeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.SortOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.sorting.GeoDistanceSorting;
import com.silverforge.elasticsearchrawclient.queryDSL.queries.sorting.ScriptBasedSorting;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = com.silverforge.elasticsearchrawclient.queryDSL.definition.QueryTest.class)
public class SortTest {

    // region Happy path

    @Test
    public void when_all_parameters_defined_then_query_generated_well() {
        ScriptBasedSorting scriptBasedSorting = mock(ScriptBasedSorting.class);
        when(scriptBasedSorting.getSortableQuery()).thenReturn("{\"_script\":{\"type\":\"number\",\"script\":{\"inline\":\"doc['field_name'].value*factor\",\"params\":{\"factor\":\"1.1\"}},\"order\":\"asc\"}}");

        GeoDistanceSorting geoDistanceSorting = mock(GeoDistanceSorting.class);
        when(geoDistanceSorting.getSortableQuery()).thenReturn("{\"_geo_distance\":{\"pin.location\":[-70, 40],\"order\":\"asc\",\"unit\":\"km\"}}");

        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        String sortableQuery = Sort
            .builder()
            .order(SortOperator.DESC)
            .fieldName("apple")
            .geoDistanceSorting(geoDistanceSorting)
            .missing(MissingOperator.LAST)
            .mode(SortModeOperator.MAX)
            .nestedFilter(matchAllQueryable)
            .nestedPath("path.of.nested.field")
            .unmappedType("number")
            .scriptBasedSorting(scriptBasedSorting)
            .build()
            .getSortableQuery();

        assertThat(sortableQuery, notNullValue());
        assertThat(sortableQuery, not(""));
    }

    // endregion

    // region Sad path

    // endregion
}
