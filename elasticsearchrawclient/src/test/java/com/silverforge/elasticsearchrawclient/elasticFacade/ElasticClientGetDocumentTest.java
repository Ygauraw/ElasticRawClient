package com.silverforge.elasticsearchrawclient.elasticFacade;

import com.silverforge.elasticsearchrawclient.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ElasticClientGetDocumentTest extends ElasticClientBaseTest {

    @Test
    public void getDocumentTest() {
        String[] ids = {"4kIU9haiQ4ysvGj4TZe0eQ", "y5llcxGFSqCsItk3VBE-7w"};

        try {
            String documents = client.getDocumentById(ids);

            assertNotNull(documents);
        } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
