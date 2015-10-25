package com.silverforge.elasticsearchrawclient.model;

import com.silverforge.elasticsearchrawclient.elasticFacade.OperationType;

import lombok.Builder;
import lombok.Getter;

@Builder
public class BulkTuple {
    @Getter
    private OperationType operationType;
    @Getter
    private String indexName;
    @Getter
    private String typeName;
    @Getter
    private String id;
    @Getter
    private Object entity;
}
