package com.silverforge.elasticsearchrawclient.elasticFacade;

import com.silverforge.elasticsearchrawclient.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ElasticClientAddDocumentTest extends ElasticClientBaseTest {

    // region Happy path

    @Test
    public void addDocumentTest() {
    }

    // endregion

    // region Sad path

    // endregion

}
