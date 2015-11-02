package com.silverforge.elasticsearchrawclient.model;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryTypeItem {
    private String name;
    private String value;
    private boolean isParent;
    private Map<String, QueryTypeItem> innerItems;
}
