package com.silverforge.elasticsearchrawclient.queryDSL.queries.sorting;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.definition.QueryTest;
import com.silverforge.elasticsearchrawclient.definition.Scriptable;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.SortOperator;

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
@Category(value = QueryTest.class)
public class ScriptBasedSortingTest {

    // region Happy path

    @Test
    public void when_all_parameters_defined_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        Scriptable scriptable = mock(Scriptable.class);
        when(scriptable.getQueryString()).thenReturn("{\"inline\":\"doc['field_name'].value*factor\",\"params\":{\"factor\":\"1.1\"}}");

        String sortableQuery = ScriptBasedSorting
            .builder()
            .order(SortOperator.DESC)
            .type("number")
            .script(scriptable)
            .build()
            .getSortableQuery();

        assertThat(sortableQuery, notNullValue());
        assertThat(sortableQuery, not(""));

        assertThat(sortableQuery.indexOf("\"order\":\"desc\""), greaterThan(0));
        assertThat(sortableQuery.indexOf("\"type\":\"number\""), greaterThan(0));
        assertThat(sortableQuery.indexOf("\"script\":{\"inline\":\"doc['field_name'].value*factor\",\"params\":{\"factor\":\"1.1\"}}"), greaterThan(0));
    }

    @Test
    public void when_minimal_expected_parameters_defined_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        Scriptable scriptable = mock(Scriptable.class);
        when(scriptable.getQueryString()).thenReturn("{\"inline\":\"doc['field_name'].value*factor\",\"params\":{\"factor\":\"1.1\"}}");

        String sortableQuery = ScriptBasedSorting
            .builder()
            .script(scriptable)
            .build()
            .getSortableQuery();

        assertThat(sortableQuery, notNullValue());
        assertThat(sortableQuery, not(""));

        assertThat(sortableQuery.indexOf("\"script\":{\"inline\":\"doc['field_name'].value*factor\",\"params\":{\"factor\":\"1.1\"}}"), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_parameters_defined_then_exception_thrown()
            throws MandatoryParametersAreMissingException {

        ScriptBasedSorting
            .builder()
            .build();
    }

    // endregion
}
