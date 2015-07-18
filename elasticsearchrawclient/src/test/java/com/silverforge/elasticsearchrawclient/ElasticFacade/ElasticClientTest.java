package com.silverforge.elasticsearchrawclient.ElasticFacade;

import com.silverforge.elasticsearchrawclient.BuildConfig;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ElasticClientTest {

    @Test
    public void searchTest() {
        assertTrue(true);
    }
}
