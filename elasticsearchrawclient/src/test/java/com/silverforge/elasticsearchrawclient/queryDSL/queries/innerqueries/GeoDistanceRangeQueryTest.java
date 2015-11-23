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

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = QueryTest.class)
public class GeoDistanceRangeQueryTest {

    // region Happy path

    @Test
    public void when_all_parameters_added_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        String queryString = GeoDistanceRangeQuery
            .builder()
            .fieldName("home.location")
            .locationGeohash("uo3543u4iy5i34uy534iuy")
            .from("200km")
            .to("400km")
            .distanceType(DistanceTypeOperator.ARC)
            .optimizeBbox(OptimizeBboxOperator.MEMORY)
            .queryName("apple")
            .coerce(true)
            .ignoreMalformed(false)
            .gt("300km")
            .lt("500km")
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"geo_distance_range\":{"), is(true));
        assertThat(queryString.endsWith("}"), is(true));

        assertThat(queryString.indexOf("\"home.location\":\"uo3543u4iy5i34uy534iuy\""), greaterThan(0));
        assertThat(queryString.indexOf("\"from\":\"200km\""), greaterThan(0));
        assertThat(queryString.indexOf("\"to\":\"400km\""), greaterThan(0));
        assertThat(queryString.indexOf("\"distance_type\":\"arc\""), greaterThan(0));
        assertThat(queryString.indexOf("\"optimize_bbox\":\"memory\""), greaterThan(0));
        assertThat(queryString.indexOf("\"_name\":\"apple\""), greaterThan(0));
        assertThat(queryString.indexOf("\"coerce\":\"true\""), greaterThan(0));
        assertThat(queryString.indexOf("\"ignore_malformed\":\"false\""), greaterThan(0));
        assertThat(queryString.indexOf("\"gt\":\"300km\""), greaterThan(0));
        assertThat(queryString.indexOf("\"lt\":\"500km\""), greaterThan(0));
    }

    @Test
    public void when_minimal_parameters_added_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        String queryString = GeoDistanceRangeQuery
            .builder()
            .fieldName("home.location")
            .location(GeoPoint.builder().longitude(23.456456456).latitude(56.324234324).build())
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"geo_distance_range\":{"), is(true));
        assertThat(queryString.endsWith("}"), is(true));

        assertThat(queryString.indexOf("\"home.location\":[23.456456456,56.324234324]"), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_parameters_added_then_exception_thrown()
        throws MandatoryParametersAreMissingException {

        GeoDistanceRangeQuery
            .builder()
            .build();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_field_name_added_then_exception_thrown()
        throws MandatoryParametersAreMissingException {

        GeoDistanceRangeQuery
            .builder()
            .locationGeohash("werhuw274234234")
            .build();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_geo_added_then_exception_thrown()
        throws MandatoryParametersAreMissingException {

        GeoDistanceRangeQuery
            .builder()
            .fieldName("home.location")
            .build();
    }

    // endregion
}
