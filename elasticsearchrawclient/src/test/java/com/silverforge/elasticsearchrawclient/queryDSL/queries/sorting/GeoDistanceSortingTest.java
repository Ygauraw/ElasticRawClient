package com.silverforge.elasticsearchrawclient.queryDSL.queries.sorting;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.GeoPoint;
import com.silverforge.elasticsearchrawclient.definition.QueryTest;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.DistanceTypeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.SortModeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.SortOperator;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = QueryTest.class)
public class GeoDistanceSortingTest {

    // region Happy path

    @Test
    public void when_all_parameters_added_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        String sortableQuery = GeoDistanceSorting
            .builder()
            .fieldName("person.location")
            .locationGeohash("b23k5jk543j4k53")
            .order(SortOperator.DESC)
            .unit("km")
            .mode(SortModeOperator.MEDIAN)
            .distanceType(DistanceTypeOperator.SLOPPY_ARC)
            .build()
            .getSortableQuery();

        assertThat(sortableQuery, notNullValue());
        assertThat(sortableQuery, not(""));

        assertThat(sortableQuery.startsWith("{\"_geo_distance\":{"), is(true));
        assertThat(sortableQuery.endsWith("}}"), is(true));

        assertThat(sortableQuery.indexOf("\"person.location\":\"b23k5jk543j4k53\""), greaterThan(0));
        assertThat(sortableQuery.indexOf("\"order\":\"desc\""), greaterThan(0));
        assertThat(sortableQuery.indexOf("\"unit\":\"km\""), greaterThan(0));
        assertThat(sortableQuery.indexOf("\"mode\":\"median\""), greaterThan(0));
        assertThat(sortableQuery.indexOf("\"distance_type\":\"sloppy_arc\""), greaterThan(0));
    }

    @Test
    public void when_minimal_parameters_added_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        String sortableQuery = GeoDistanceSorting
            .builder()
            .fieldName("person.location")
            .location(GeoPoint.builder().longitude(32.7894823).latitude(23.432894728).build())
            .build()
            .getSortableQuery();

        assertThat(sortableQuery, notNullValue());
        assertThat(sortableQuery, not(""));

        assertThat(sortableQuery.startsWith("{\"_geo_distance\":{"), is(true));
        assertThat(sortableQuery.endsWith("}}"), is(true));

        assertThat(sortableQuery.indexOf("\"person.location\":[32.7894823,23.432894728]"), greaterThan(0));
    }

    @Test
    public void when_multiple_location_parameters_added_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        String sortableQuery = GeoDistanceSorting
            .builder()
            .fieldName("person.location")
            .locations(
                GeoPoint.builder().longitude(32.7894823).latitude(23.432894728).build(),
                GeoPoint.builder().longitude(23.4324).latitude(34.23432).build(),
                GeoPoint.builder().longitude(28.4).latitude(31.2).build())
            .build()
            .getSortableQuery();

        assertThat(sortableQuery, notNullValue());
        assertThat(sortableQuery, not(""));

        assertThat(sortableQuery.startsWith("{\"_geo_distance\":{"), is(true));
        assertThat(sortableQuery.endsWith("}}"), is(true));

        assertThat(sortableQuery.indexOf("\"person.location\":[[32.7894823,23.432894728],[23.4324,34.23432],[28.4,31.2]]"), greaterThan(0));
    }

    @Test
    public void when_multiple_location_geohash_parameters_added_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        String sortableQuery = GeoDistanceSorting
            .builder()
            .fieldName("person.location")
            .locationGeohashes(
                "uio453uio435oiu345",
                "bhk345bhj345hbkj345",
                "345bhj345hjb345")
            .build()
            .getSortableQuery();

        assertThat(sortableQuery, notNullValue());
        assertThat(sortableQuery, not(""));

        assertThat(sortableQuery.startsWith("{\"_geo_distance\":{"), is(true));
        assertThat(sortableQuery.endsWith("}}"), is(true));

        assertThat(sortableQuery.indexOf("\"person.location\":[\"uio453uio435oiu345\",\"bhk345bhj345hbkj345\",\"345bhj345hjb345\"]"), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_parameters_added_then_exception_thrown()
            throws MandatoryParametersAreMissingException {

        GeoDistanceSorting
            .builder()
            .build();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_only_the_field_parameter_added_then_exception_thrown()
            throws MandatoryParametersAreMissingException {

        GeoDistanceSorting
            .builder()
            .fieldName("pin.location")
            .build();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_only_the_value_parameter_added_then_exception_thrown()
            throws MandatoryParametersAreMissingException {

        GeoDistanceSorting
            .builder()
            .locationGeohash("u803uig3bhkbhjkr3we")
            .build();
    }

    // endregion
}
