package com.silverforge.elasticsearchrawclient.elasticFacade.model;

import java.util.HashMap;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ElasticSettings {
    private String[] indices;
    private String[] types;
    private HashMap<String, String[]> properties;
}
