package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.GeoPoint;
import com.silverforge.elasticsearchrawclient.definition.QueryTest;

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
public class GeohashCellQueryTest {

    // region Happy path

    @Test
    public void when_all_parameters_defined_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        String queryString = GeohashCellQuery
            .builder()
            .fieldName("agent.location")
            .location(GeoPoint.builder().longitude(89.34537249382).latitude(34.831342444).build())
            .precision("3m")
            .neighbors(false)
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"geohash_cell\":{"), is(true));
        assertThat(queryString.endsWith("}"), is(true));

        assertThat(queryString.indexOf("\"lat\":34.831342444"), greaterThan(0));
        assertThat(queryString.indexOf("\"lon\":89.34537249382"), greaterThan(0));
        assertThat(queryString.indexOf("{\"lat\":34.831342444,\"lon\":89.34537249382}"), greaterThan(0));

        assertThat(queryString.indexOf("\"precision\":\"3m\""), greaterThan(0));
        assertThat(queryString.indexOf("\"neighbors\":\"false\""), greaterThan(0));
    }

    @Test
    public void when_minimum_required_parameters_defined_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        String queryString = GeohashCellQuery
            .builder()
            .fieldName("agent.location")
            .location(GeoPoint.builder().longitude(89.34537249382).latitude(34.831342444).build())
            .build()
            .getQueryString();

        assertThat(queryString, notNullValue());
        assertThat(queryString, not(""));

        assertThat(queryString.startsWith("{\"geohash_cell\":{"), is(true));
        assertThat(queryString.endsWith("}"), is(true));

        assertThat(queryString.indexOf("\"lat\":34.831342444"), greaterThan(0));
        assertThat(queryString.indexOf("\"lon\":89.34537249382"), greaterThan(0));
        assertThat(queryString.indexOf("{\"lat\":34.831342444,\"lon\":89.34537249382}"), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_no_parameters_defined_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        GeohashCellQuery
            .builder()
            .build()
            .getQueryString();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_only_the_field_parameter_defined_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        GeohashCellQuery
            .builder()
            .fieldName("dhs")
            .build()
            .getQueryString();
    }

    @Test(expected = MandatoryParametersAreMissingException.class)
    public void when_only_the_location_parameter_defined_then_query_generated_well()
            throws MandatoryParametersAreMissingException {

        GeohashCellQuery
            .builder()
            .location(GeoPoint.builder().longitude(23.34).latitude(456.456456).build())
            .build()
            .getQueryString();
    }

    //
}
