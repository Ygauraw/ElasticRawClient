package com.silverforge.webconnector.integration.sync;

import com.silverforge.webconnector.BuildConfig;
import com.silverforge.webconnector.EndpointConnector;
import com.silverforge.webconnector.definition.HttpTest;
import com.silverforge.webconnector.exceptions.SettingsIsNullException;
import com.silverforge.webconnector.model.ConnectorSettings;
import com.silverforge.webconnector.model.InvokeBinaryResult;
import com.silverforge.webconnector.model.InvokeStringResult;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.net.URISyntaxException;
import java.net.UnknownHostException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = HttpTest.class)
public class InvokeToHttpEndpointTest {

    private EndpointConnector connector;

    public InvokeToHttpEndpointTest() {
        ConnectorSettings settings = ConnectorSettings
                .builder()
                .baseUrl("http://www.szerencsejatek.hu")
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
        InvokeStringResult invokeStringResult = connector.get("/xls/otos.csv");

        assertThat(invokeStringResult, notNullValue());
        assertThat(invokeStringResult.getAggregatedExceptions(), notNullValue());
        assertThat(invokeStringResult.getAggregatedExceptions().size(), is(0));
        assertThat(invokeStringResult.isSuccess(), is(true));
        assertThat(invokeStringResult.getResult(), notNullValue());
    }

    @Test
    public void invokeBinaryResultTest() {
        InvokeBinaryResult invokeStringResult = connector.readBinaryContent("/xls/otos.xls");

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
                .baseUrl("http://www.almabarack.pre")
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
