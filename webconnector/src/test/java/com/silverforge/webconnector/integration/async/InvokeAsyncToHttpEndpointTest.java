package com.silverforge.webconnector.integration.async;

import com.silverforge.webconnector.BuildConfig;
import com.silverforge.webconnector.EndpointConnector;
import com.silverforge.webconnector.definition.HttpTest;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Category(value = HttpTest.class)
public class InvokeAsyncToHttpEndpointTest {

    private EndpointConnector connector;

    public InvokeAsyncToHttpEndpointTest() {
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
    public void invokeAsyncStringResultTest() {

        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        connector
            .getAsync("/xls/otos.csv")
            .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertUnsubscribed();

        List<String> stringList = testSubscriber.getOnNextEvents();

        assertThat(stringList, notNullValue());
        assertThat(stringList.size(), is(1));
        assertThat(stringList.get(0), notNullValue());
        assertThat(stringList.get(0), not(equalTo("")));
    }

    @Test
    public void invokeAsyncBinaryResultTest() {

        TestSubscriber<byte[]> testSubscriber = new TestSubscriber<>();

        connector
            .readBinaryContentAsync("/xls/otos.xls")
            .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertUnsubscribed();

        List<byte[]> byteChunks = testSubscriber.getOnNextEvents();

        assertThat(byteChunks, notNullValue());
        assertThat(byteChunks.size(), greaterThan(0));
    }

    // endregion

    // region Sad path

    @Test
    public void invokeAsyncNoServerTest()
            throws SettingsIsNullException, URISyntaxException {

        ConnectorSettings settings = ConnectorSettings
                .builder()
                .baseUrl("http://www.almabarack.pre")
                .build();

        EndpointConnector conn = new EndpointConnector(settings);

        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        conn
            .getAsync("")
            .subscribe(testSubscriber);

        testSubscriber.assertUnsubscribed();
        List<Throwable> errorEvents = testSubscriber.getOnErrorEvents();

        assertThat(errorEvents, notNullValue());
        assertThat(errorEvents.size(), is(1));
        assertThat(errorEvents.get(0), instanceOf(UnknownHostException.class));
    }

    // endregion
}
