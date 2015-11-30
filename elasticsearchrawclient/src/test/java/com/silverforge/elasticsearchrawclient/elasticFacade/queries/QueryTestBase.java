package com.silverforge.elasticsearchrawclient.elasticFacade.queries;

import com.silverforge.elasticsearchrawclient.elasticFacade.ElasticClient;
import com.silverforge.elasticsearchrawclient.model.ElasticSettings;
import com.silverforge.webconnector.exceptions.SettingsIsNullException;
import com.silverforge.webconnector.model.ConnectorSettings;

import java.net.URISyntaxException;

public abstract class QueryTestBase {

    protected ElasticClient client;

    protected QueryTestBase() {
        ConnectorSettings settings = ConnectorSettings
            .builder()
            .baseUrl("http://localhost:9200")
            .build();

        ElasticSettings elasticSettings = ElasticSettings
            .builder()
            .indices(new String[]{"cities"})
            .build();

        try {
            client = new ElasticClient(settings, elasticSettings);
        } catch (SettingsIsNullException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
