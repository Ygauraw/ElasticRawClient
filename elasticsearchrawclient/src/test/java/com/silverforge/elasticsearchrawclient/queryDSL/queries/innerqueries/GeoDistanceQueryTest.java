package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.GeoPoint;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.QueryTest;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.DistanceTypeOperator;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.OptimizeBboxOperator;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = QueryTest.class)
public class GeoDistanceQueryTest {

    // region Happy path

    @Test
    public void when_all_parameters_added_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        String queryString = GeoDistanceQuery
            .builder()
            .fieldName("house.location")
            .location(GeoPoint.builder().longitude(34.4795322).latitude(43.6347854379).build())
            .distance("2km")
            .distanceType(DistanceTypeOperator.ARC)
            .optimizeBbox(OptimizeBboxOperator.MEMORY)
            .queryName("apple")
            .coerce(true)
            .ignoreMalformed(false)
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"geo_distance\":{"), is(true));
        assertThat(queryString.endsWith("}"), is(true));

        assertThat(queryString.indexOf("\"house.location\":[34.4795322,43.6347854379]"), greaterThan(0));
        assertThat(queryString.indexOf("\"distance\":\"2km\""), greaterThan(0));
        assertThat(queryString.indexOf("\"distance_type\":\"arc\""), greaterThan(0));
        assertThat(queryString.indexOf("\"optimize_bbox\":\"memory\""), greaterThan(0));
        assertThat(queryString.indexOf("\"_name\":\"apple\""), greaterThan(0));
        assertThat(queryString.indexOf("\"coerce\":\"true\""), greaterThan(0));
        assertThat(queryString.indexOf("\"ignore_malformed\":\"false\""), greaterThan(0));
    }

    @Test
    public void when_minimal_parameters_added_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        String queryString = GeoDistanceQuery
            .builder()
            .fieldName("house.location")
            .location(GeoPoint.builder().longitude(34.4795322).latitude(43.6347854379).build())
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"geo_distance\":{"), is(true));
        assertThat(queryString.endsWith("}"), is(true));

        assertThat(queryString.indexOf("\"house.location\":[34.4795322,43.6347854379]"), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_parameters_added_then_exception_thrown()
            throws MandatoryParametersAreMissingException {

        GeoDistanceQuery
            .builder()
            .build();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_only_fieldName_parameter_added_then_exception_thrown()
            throws MandatoryParametersAreMissingException {

        GeoDistanceQuery
            .builder()
            .fieldName("alma")
            .build();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_only_location_parameter_added_then_exception_thrown()
            throws MandatoryParametersAreMissingException {

        GeoDistanceQuery
            .builder()
            .locationGeohash("h23orowrehiurw")
            .build();
    }

    // endregion
}
