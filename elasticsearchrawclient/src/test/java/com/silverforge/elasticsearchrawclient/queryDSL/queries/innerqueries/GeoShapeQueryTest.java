package com.silverforge.elasticsearchrawclient.queryDSL.queries.innerQueries;

import com.silverforge.elasticsearchrawclient.BuildConfig;
import com.silverforge.elasticsearchrawclient.exceptions.MandatoryParametersAreMissingException;
import com.silverforge.elasticsearchrawclient.model.GeoPoint;
import com.silverforge.elasticsearchrawclient.queryDSL.operators.GeoShapeTypeOperator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
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
                    .latitude(4.5652f)
                    .longitude(6.24345f)
                    .build())
                .build()
                .getQueryString();

            assertThat(queryString, notNullValue());
            assertThat(queryString, not(""));
        } catch (MandatoryParametersAreMissingException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    // endregion

    // region Sad path

    // endregion
}
