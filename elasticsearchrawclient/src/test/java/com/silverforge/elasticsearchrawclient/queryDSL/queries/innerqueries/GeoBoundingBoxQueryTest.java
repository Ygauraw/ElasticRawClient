package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.GeoPoint;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.QueryTest;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.GeoBoundingBoxTypeOperator;

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
public class GeoBoundingBoxQueryTest {

    // region Happy path

    @Test
    public void when_top_left_and_bottom_right_parameters_defined_then_query_is_generated_well()
            throws MandatoryParametersAreMissingException {

        String queryString = GeoBoundingBoxQuery
            .builder()
            .fieldName("house.location")
            .topLeft(GeoPoint.builder().longitude(45.454f).latitude(42.34234f).build())
            .bottomRight(GeoPoint.builder().longitude(45.87543f).latitude(42.123f).build())
            .type(GeoBoundingBoxTypeOperator.INDEXED)
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"geo_bounding_box\":{"), is(true));
        assertThat(queryString.endsWith("}"), is(true));

        assertThat(queryString.indexOf("\"house.location\":{"), greaterThan(0));
        assertThat(queryString.indexOf("\"top_left\":[45.454,42.34234]"), greaterThan(0));
        assertThat(queryString.indexOf("\"bottom_right\":[45.87543,42.123]"), greaterThan(0));
        assertThat(queryString.indexOf("\"type\":\"indexed\""), greaterThan(0));
    }

    @Test
    public void when_top_left_and_bottom_right_geohash_parameters_defined_then_query_is_generated_well()
            throws MandatoryParametersAreMissingException {

        String queryString = GeoBoundingBoxQuery
            .builder()
            .fieldName("house.location")
            .topLeftGeohash("ehriwq3284u")
            .bottomRightGeohash("a43545trdfs")
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"geo_bounding_box\":{"), is(true));
        assertThat(queryString.endsWith("}"), is(true));

        assertThat(queryString.indexOf("\"house.location\":{"), greaterThan(0));
        assertThat(queryString.indexOf("\"top_left\":\"ehriwq3284u\""), greaterThan(0));
        assertThat(queryString.indexOf("\"bottom_right\":\"a43545trdfs\""), greaterThan(0));
    }

    @Test
    public void when_top_right_and_bottom_left_parameters_defined_then_query_is_generated_well()
            throws MandatoryParametersAreMissingException {

        String queryString = GeoBoundingBoxQuery
            .builder()
            .fieldName("house.location")
            .topRight(GeoPoint.builder().longitude(45.454f).latitude(42.34234f).build())
            .bottomLeft(GeoPoint.builder().longitude(45.87543f).latitude(42.123f).build())
            .type(GeoBoundingBoxTypeOperator.MEMORY)
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"geo_bounding_box\":{"), is(true));
        assertThat(queryString.endsWith("}"), is(true));

        assertThat(queryString.indexOf("\"house.location\":{"), greaterThan(0));
        assertThat(queryString.indexOf("\"top_right\":[45.454,42.34234]"), greaterThan(0));
        assertThat(queryString.indexOf("\"bottom_left\":[45.87543,42.123]"), greaterThan(0));
        assertThat(queryString.indexOf("\"type\":\"memory\""), greaterThan(0));
    }

    @Test
    public void when_top_right_and_bottom_left_geohash_parameters_defined_then_query_is_generated_well()
            throws MandatoryParametersAreMissingException {

        String queryString = GeoBoundingBoxQuery
            .builder()
            .fieldName("house.location")
            .topRightGeohash("3iyu4iuy34u4yi3")
            .bottomLeftGeohash("u4oiuroiuwwro32324i23")
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"geo_bounding_box\":{"), is(true));
        assertThat(queryString.endsWith("}"), is(true));

        assertThat(queryString.indexOf("\"house.location\":{"), greaterThan(0));
        assertThat(queryString.indexOf("\"top_right\":\"3iyu4iuy34u4yi3\""), greaterThan(0));
        assertThat(queryString.indexOf("\"bottom_left\":\"u4oiuroiuwwro32324i23\""), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test
    public void when_top_right_and_top_left_geohash_parameters_defined_then_query_is_generated_well()
            throws MandatoryParametersAreMissingException {

        String queryString = GeoBoundingBoxQuery
            .builder()
            .fieldName("house.location")
            .topRightGeohash("3iyu4iuy34u4yi3")
            .topLeftGeohash("u4oiuroiuwwro32324i23")
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"geo_bounding_box\":{"), is(true));
        assertThat(queryString.endsWith("}"), is(true));

        assertThat(queryString.indexOf("\"house.location\":{"), greaterThan(0));
        assertThat(queryString.indexOf("\"top_right\":\"3iyu4iuy34u4yi3\""), greaterThan(0));
        assertThat(queryString.indexOf("\"top_left\":\"u4oiuroiuwwro32324i23\""), greaterThan(0));
    }

    @Test
    public void when_field_name_is_defined_then_query_is_generated_well()
            throws MandatoryParametersAreMissingException {

        String queryString = GeoBoundingBoxQuery
            .builder()
            .fieldName("house.location")
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"geo_bounding_box\":{"), is(true));
        assertThat(queryString.endsWith("}"), is(true));

        assertThat(queryString.indexOf("\"house.location\":{"), greaterThan(0));
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_parameters_defined_then_exception_thrown()
            throws MandatoryParametersAreMissingException {

        GeoBoundingBoxQuery
            .builder()
            .build();
    }

    // endregion
}
