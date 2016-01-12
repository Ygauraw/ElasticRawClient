package com.silverforge.webconnector;

import com.silverforge.webconnector.exceptions.SettingsIsNullException;
import com.silverforge.webconnector.model.ConnectorSettings;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ConnectorTest {

    // region Happy path

    @Test
    public void getSettingsTest() {
        try {
            ConnectorSettings connectorSettings = ConnectorSettings
                    .builder()
                    .baseUrl("http://my.custom.org")
                    .userName("myUserName")
                    .password("myPassword")
                    .readTimeout(1000)
                    .connectTimeout(4000)
                    .retryCount(4)
                    .build();

            Connector connector = new Connector(connectorSettings);
            ConnectorSettings retSettings = connector.getSettings();

            assertThat(retSettings, notNullValue());
            assertThat(retSettings.getUri(), equalTo(new URI("http://my.custom.org")));
            assertThat(retSettings.getBaseUrl(), equalTo(connectorSettings.getBaseUrl()));
            assertThat(retSettings.getUserName(), equalTo(connectorSettings.getUserName()));
            assertThat(retSettings.getPassword(), equalTo(connectorSettings.getPassword()));
            assertThat(retSettings.getReadTimeout(), equalTo(connectorSettings.getReadTimeout()));
            assertThat(retSettings.getConnectTimeout(), equalTo(connectorSettings.getConnectTimeout()));
            assertThat(retSettings.getRetryCount(), equalTo(connectorSettings.getRetryCount()));
        } catch (SettingsIsNullException | URISyntaxException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    // endregion


    // region Sad path

    @Test(expected = SettingsIsNullException.class)
    public void getSettingsNullTest()
            throws SettingsIsNullException {

        try {
            new Connector(null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test(expected = URISyntaxException.class)
    public void getSettingsBadUriTest()
            throws URISyntaxException {

        try {
            ConnectorSettings connectorSettings = ConnectorSettings
                    .builder()
                    .baseUrl("http://finance.yahoo.com/q/h?s=^IXIC")
                    .userName("myUserName")
                    .password("myPassword")
                    .readTimeout(1000)
                    .connectTimeout(4000)
                    .retryCount(4)
                    .build();

            new Connector(connectorSettings);
        } catch (SettingsIsNullException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void getSettingsEmptyTest() {
        try {
            String defaultUrl = "http://localhost";
            String stringEmpty = "";
            int defaultTimeout = 7000;
            int defaultRetryCount = 3;

            Connector connector = new Connector(ConnectorSettings.builder().build());
            ConnectorSettings retSettings = connector.getSettings();

            assertThat(retSettings, notNullValue());
            assertThat(retSettings.getUri(), equalTo(new URI(defaultUrl)));
            assertThat(retSettings.getBaseUrl(), equalTo(defaultUrl));
            assertThat(retSettings.getUserName(), equalTo(stringEmpty));
            assertThat(retSettings.getPassword(), equalTo(stringEmpty));
            assertThat(retSettings.getReadTimeout(), equalTo(defaultTimeout));
            assertThat(retSettings.getConnectTimeout(), equalTo(defaultTimeout));
            assertThat(retSettings.getRetryCount(), equalTo(defaultRetryCount));
        } catch (SettingsIsNullException | URISyntaxException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    // endregion
}
