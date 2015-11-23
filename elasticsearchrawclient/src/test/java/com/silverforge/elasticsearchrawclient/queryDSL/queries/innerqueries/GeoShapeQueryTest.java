package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.GeoPoint;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.QueryTest;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.GeoShapeTypeOperator;

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
import static org.junit.Assert.fail;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = QueryTest.class)
public class GeoShapeQueryTest {

    // region Happy path

    @Test
    public void when_shape_added_then_type_and_coordinate_should_be_generated() {
        try {
            String queryString = GeoShapeQuery
                .builder()
                .type(GeoShapeTypeOperator.POINT)
                .coordinates(GeoPoint
                    .builder()
                    .latitude(4.5652)
                    .longitude(6.24345)
                    .build())
                .build()
                .getQueryString();

            assertThat(queryString, notNullValue());
            assertThat(queryString, not(""));

            assertThat(queryString.startsWith("{\"geo_shape\":{\"_all\":{\"shape\":{"), is(true));
            assertThat(queryString.endsWith("}}}}"), is(true));

            assertThat(queryString.indexOf("\"type\":\"point\""), greaterThan(0));
            assertThat(queryString.indexOf("\"coordinates\":[6.24345,4.5652]"), greaterThan(0));
        } catch (MandatoryParametersAreMissingException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void when_shape_added_with_field_name_then_type_and_coordinate_should_be_generated() {
        try {
            String queryString = GeoShapeQuery
                .builder()
                .fieldName("myFunnyLocations")
                .type(GeoShapeTypeOperator.POINT)
                .coordinates(GeoPoint
                    .builder()
                    .latitude(4.5652)
                    .longitude(6.24345)
                    .build())
                .build()
                .getQueryString();

            assertThat(queryString, notNullValue());
            assertThat(queryString, not(""));

            assertThat(queryString.startsWith("{\"geo_shape\":{\"myFunnyLocations\":{\"shape\":{"), is(true));
            assertThat(queryString.endsWith("}}}"), is(true));

            assertThat(queryString.indexOf("\"type\":\"point\""), greaterThan(0));
            assertThat(queryString.indexOf("\"coordinates\":[6.24345,4.5652]"), greaterThan(0));
        } catch (MandatoryParametersAreMissingException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void when_indexedshape_added_with_field_name_then_index_related_query_should_be_generated() {
        try {
            String queryString = GeoShapeQuery
                .builder()
                .fieldName("myFunnyLocations")
                .indexedShape()
                .id("2")
                .type("apple")
                .index("apples")
                .path("myloc")
                .build()
                .getQueryString();

            assertThat(queryString, notNullValue());
            assertThat(queryString, not(""));

            assertThat(queryString.startsWith("{\"geo_shape\":{\"myFunnyLocations\":{\"indexed_shape\":{"), is(true));
            assertThat(queryString.endsWith("}}}"), is(true));

            assertThat(queryString.indexOf("\"id\":\"2\""), greaterThan(0));
            assertThat(queryString.indexOf("\"type\":\"apple\""), greaterThan(0));
            assertThat(queryString.indexOf("\"index\":\"apples\""), greaterThan(0));
            assertThat(queryString.indexOf("\"path\":\"myloc\""), greaterThan(0));
        } catch (MandatoryParametersAreMissingException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    // endregion

    // region Sad path

    @Test
    public void when_no_parameters_defined_then_type_and_coordinate_should_be_generated() {
        try {
            String queryString = GeoShapeQuery
                .builder()
                .build()
                .getQueryString();

            assertThat(queryString, notNullValue());
            assertThat(queryString, not(""));

            assertThat(queryString.startsWith("{\"geo_shape\":{\"_all\":{\"shape\":{"), is(true));
            assertThat(queryString.endsWith("}}}"), is(true));
        } catch (MandatoryParametersAreMissingException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_parameters_defined_with_indexedshape_then_exception_is_thrown()
            throws MandatoryParametersAreMissingException {

        GeoShapeQuery
            .builder()
            .indexedShape()
            .build()
            .getQueryString();
    }

    // endregion
}
