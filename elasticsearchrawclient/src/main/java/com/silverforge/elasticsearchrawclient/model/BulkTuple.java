package com.silverforge.elasticsearchrawclient.model;

import com.silverforge.elasticsearchrawclient.elasticFacade.OperationType;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class BulkTuple {
    private OperationType operationType;
    private String indexName;
    private String typeName;
    private String id;
    private Object entity;
}
