package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.GeoPoint;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.QueryTest;

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
public class GeoPolygonQueryTest {

    // region Happy path

    @Test
    public void when_all_parameters_defined_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        GeoPoint geoPoint1 = GeoPoint.builder().longitude(12.123456).latitude(23.23456789).build();
        GeoPoint geoPoint2 = GeoPoint.builder().longitude(34.34567890).latitude(45.4567).build();
        GeoPoint geoPoint3 = GeoPoint.builder().longitude(56.567).latitude(67.67890123).build();

        String queryString = GeoPolygonQuery
            .builder()
            .fieldName("location")
            .points(geoPoint1, geoPoint2, geoPoint3)
            .queryName("MyWonderfulQuery")
            .coerce(false)
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"geo_polygon\":{"), is(true));
        assertThat(queryString.endsWith("}}"), is(true));

        assertThat(queryString.indexOf("\"location\":{"), greaterThan(0));

        assertThat(queryString.indexOf("[12.123456,23.23456789]"), greaterThan(0));
        assertThat(queryString.indexOf("[34.3456789,45.4567]"), greaterThan(0));
        assertThat(queryString.indexOf("[56.567,67.67890123]"), greaterThan(0));

        assertThat(queryString.indexOf("\"_name\":\"MyWonderfulQuery\""), greaterThan(0));
        assertThat(queryString.indexOf("\"coerce\":\"false\""), greaterThan(0));
    }

    @Test
    public void when_minimal_parameters_defined_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        GeoPoint geoPoint1 = GeoPoint.builder().longitude(12.123456).latitude(23.23456789).build();
        GeoPoint geoPoint2 = GeoPoint.builder().longitude(34.34567890).latitude(45.4567).build();
        GeoPoint geoPoint3 = GeoPoint.builder().longitude(56.567).latitude(67.67890123).build();

        String queryString = GeoPolygonQuery
            .builder()
            .fieldName("location")
            .points(geoPoint1, geoPoint2, geoPoint3)
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"geo_polygon\":{\"location\":{"), is(true));
        assertThat(queryString.endsWith("}}}"), is(true));

        assertThat(queryString.indexOf("[12.123456,23.23456789]"), greaterThan(0));
        assertThat(queryString.indexOf("[34.3456789,45.4567]"), greaterThan(0));
        assertThat(queryString.indexOf("[56.567,67.67890123]"), greaterThan(0));
    }

    // endregion

    //region Sad path

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_parameters_defined_then_exception_thrown()
            throws MandatoryParametersAreMissingException {

        GeoPolygonQuery
            .builder()
            .build();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_only_field_name_parameter_defined_then_exception_thrown()
            throws MandatoryParametersAreMissingException {

        GeoPolygonQuery
            .builder()
            .fieldName("a")
            .build();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_only_points_parameter_defined_then_exception_thrown()
            throws MandatoryParametersAreMissingException {

        GeoPolygonQuery
            .builder()
            .points("bhj4giy3giu435ui")
            .build();
    }

    // endregion
}
