package com.silverforge.elasticsearchrawclient.elasticFacade.model;

import com.silverforge.elasticsearchrawclient.elasticFacade.OperationType;

import lombok.Getter;

public class BulkActionResult {

    @Getter
    private BulkTuple bulkTuple;
    @Getter
    private OperationType operation;
    @Getter
    private String indexName;
    @Getter
    private String typeName;
    @Getter
    private String id;
    @Getter
    private Integer version;
    @Getter
    private Integer status;
    @Getter
    private Boolean found;

    public BulkActionResult(OperationType operation, String indexName, String typeName, String id, Integer version, Integer status, Boolean found, BulkTuple bulkTuple){
        this.operation = operation;
        this.indexName = indexName;
        this.typeName = typeName;
        this.id = id;
        this.version = version;
        this.status = status;
        this.found = found;
        this.bulkTuple = bulkTuple;
    }
}
