package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.queryDSL.definition.QueryTest;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

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
            .build()
            .getQueryString();


    }

    // endregion

    // region Sad path



    // endregion
}
