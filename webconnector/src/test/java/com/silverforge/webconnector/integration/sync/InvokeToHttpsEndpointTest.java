package com.silverforge.webconnector.integration.sync;

import com.silverforge.webconnector.BuildConfig;
import com.silverforge.webconnector.EndpointConnector;
import com.silverforge.webconnector.definition.HttpsTest;
import com.silverforge.webconnector.exceptions.SettingsIsNullException;
import com.silverforge.webconnector.model.ConnectorSettings;
import com.silverforge.webconnector.model.InvokeStringResult;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.net.URISyntaxException;
import java.net.UnknownHostException;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = HttpsTest.class)
public class InvokeToHttpsEndpointTest {

    private static final String ELASTIC_URL = "https://mgj.east-us.azr.facetflow.io";
    private static final String ELASTIC_APIKEY = "wihIilbbekmCeppKlgQXDwpSZEUekkk0";

    private EndpointConnector connector;

    public InvokeToHttpsEndpointTest() {
        ConnectorSettings settings = ConnectorSettings
                .builder()
                .baseUrl(ELASTIC_URL)
                .userName(ELASTIC_APIKEY)
                .build();

        try {
            connector = new EndpointConnector(settings);
        } catch (SettingsIsNullException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    // region Happy path

    @Test
    public void invokeStringResultTest() {
        InvokeStringResult invokeStringResult = connector.get("/cities/_search");

        assertThat(invokeStringResult, notNullValue());
        assertThat(invokeStringResult.getAggregatedExceptions(), notNullValue());
        assertThat(invokeStringResult.getAggregatedExceptions().size(), is(0));
        assertThat(invokeStringResult.isSuccess(), is(true));
        assertThat(invokeStringResult.getResult(), notNullValue());
    }

    // endregion

    // region Sad path

    @Test
    public void invokeNoServerTest()
            throws SettingsIsNullException, URISyntaxException {

        ConnectorSettings settings = ConnectorSettings
                .builder()
                .baseUrl("https://www.almabarack.pre")
                .build();

        EndpointConnector conn = new EndpointConnector(settings);
        InvokeStringResult invokeStringResult = conn.get("");

        assertThat(invokeStringResult, notNullValue());
        assertThat(invokeStringResult.isSuccess(), is(false));
        assertThat(invokeStringResult.getResult(), nullValue());
        assertThat(invokeStringResult.getStatusCode(), nullValue());
        assertThat(invokeStringResult.getAggregatedExceptions(), notNullValue());
        assertThat(invokeStringResult.getAggregatedExceptions().size(), greaterThan(0));
        assertThat(invokeStringResult.getAggregatedExceptions().get(0), instanceOf(UnknownHostException.class));
    }

    // endregion
}
