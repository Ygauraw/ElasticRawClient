package com.silverforge.elasticsearchrawclient.queryDSL.queries.sorting;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.definition.Queryable;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.MissingOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.SortModeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.SortOperator;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = com.silverforge.elasticsearchrawclient.definition.QueryTest.class)
public class SortingTest {

    // region Happy path

    @Test
    public void when_all_parameters_defined_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        Queryable matchAllQueryable = mock(Queryable.class);
        when(matchAllQueryable.getQueryString()).thenReturn("{\"match_all\":{}}");

        String sortableQuery = Sorting
            .builder()
            .order(SortOperator.DESC)
            .fieldName("apple")
            .missing(MissingOperator.LAST)
            .mode(SortModeOperator.MAX)
            .nestedFilter(matchAllQueryable)
            .nestedPath("path.of.nested.field")
            .unmappedType("number")
            .build()
            .getSortableQuery();

        assertThat(sortableQuery, notNullValue());
        assertThat(sortableQuery, not(""));

        assertThat(sortableQuery.startsWith("{\"apple\":{"), is(true));
        assertThat(sortableQuery.endsWith("}}"), is(true));

        assertThat(sortableQuery.indexOf("\"order\":\"desc\""), greaterThan(0));
        assertThat(sortableQuery.indexOf("\"missing\":\"last\""), greaterThan(0));
        assertThat(sortableQuery.indexOf("\"mode\":\"max\""), greaterThan(0));
        assertThat(sortableQuery.indexOf("\"nested_filter\":{\"match_all\":{}}"), greaterThan(0));
        assertThat(sortableQuery.indexOf("\"nested_path\":\"path.of.nested.field\""), greaterThan(0));
        assertThat(sortableQuery.indexOf("\"unmapped_type\":\"number\""), greaterThan(0));
    }

    @Test
    public void when_some_parameters_defined_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        String sortableQuery = Sorting
            .builder()
            .order(SortOperator.DESC)
            .fieldName("apple")
            .build()
            .getSortableQuery();

        assertThat(sortableQuery, notNullValue());
        assertThat(sortableQuery, not(""));

        assertThat(sortableQuery.startsWith("{\"apple\":{"), is(true));
        assertThat(sortableQuery.endsWith("}}"), is(true));

        assertThat(sortableQuery.indexOf("\"order\":\"desc\""), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_parameters_defined_then_exception_thrown()
        throws MandatoryParametersAreMissingException {

        Sorting
            .builder()
            .build()
            .getSortableQuery();
    }

    // endregion
}
