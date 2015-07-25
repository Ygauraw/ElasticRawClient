package com.silverforge.elasticsearchrawclient.elasticFacade;

import com.silverforge.elasticsearchrawclient.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ElasticClientSearchTest extends ElasticClientBaseTest {
    // region Happy path

    @Test
    public void searchTest() {
        try {
            String search = client.search("{\"query\":{\"match_all\": {}}}");

            assertNotNull(search);
        } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    // endregion

    // region Sad path

    @Test
    public void searchEmptyQueryTest() {
        try {
            String search = client.search("{}");

            assertNotNull(search);
        } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void searchNullTest() {
        try {
            String search = client.search(null);

            assertNotNull(search);
        } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void searchEmptyTest() {
        try {
            String search = client.search("");

            assertNotNull(search);
        } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    // endregion
}
