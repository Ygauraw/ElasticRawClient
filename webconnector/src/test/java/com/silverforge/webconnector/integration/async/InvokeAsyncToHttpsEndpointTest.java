package com.silverforge.webconnector.integration.async;

import com.silverforge.webconnector.BuildConfig;
import com.silverforge.webconnector.EndpointConnector;
import com.silverforge.webconnector.definition.HttpsTest;
import com.silverforge.webconnector.exceptions.SettingsIsNullException;
import com.silverforge.webconnector.model.ConnectorSettings;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.List;

import rx.observers.TestSubscriber;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = HttpsTest.class)
public class InvokeAsyncToHttpsEndpointTest {

    private static final String ELASTIC_URL = "https://mgj.east-us.azr.facetflow.io";
    private static final String ELASTIC_APIKEY = "wihIilbbekmCeppKlgQXDwpSZEUekkk0";

    private EndpointConnector connector;

    public InvokeAsyncToHttpsEndpointTest() {
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
    public void invokeAsyncStringResultTest() {

        TestSubscriber<String> testSubscriber = new TestSubscriber<>();

        connector.getAsync("/cities/_search").subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertUnsubscribed();

        List<String> responseList = testSubscriber.getOnNextEvents();

        assertThat(responseList, notNullValue());
        assertThat(responseList.size(), is(1));
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

        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        conn
            .getAsync("")
            .subscribe(testSubscriber);

        testSubscriber.assertUnsubscribed();
        List<Throwable> throwables = testSubscriber.getOnErrorEvents();

        assertThat(throwables, notNullValue());
        assertThat(throwables.size(), is(1));
        assertThat(throwables.get(0), instanceOf(UnknownHostException.class));
    }

    // endregion
}
