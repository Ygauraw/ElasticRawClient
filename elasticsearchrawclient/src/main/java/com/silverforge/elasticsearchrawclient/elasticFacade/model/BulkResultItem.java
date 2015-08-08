package com.silverforge.elasticsearchrawclient.elasticFacade.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.silverforge.elasticsearchrawclient.elasticFacade.OperationType;

import lombok.Getter;
import lombok.Setter;

public class BulkResultItem {

    @Getter
    @Setter
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

    public BulkResultItem(
            @JsonProperty("_index")
            String indexName,
            @JsonProperty("_type")
            String typeName,
            @JsonProperty("_id")
            String id,
            @JsonProperty("_version")
            Integer version,
            @JsonProperty("status")
            Integer status,
            @JsonProperty("found")
            Boolean found){

        this.indexName = indexName;
        this.typeName = typeName;
        this.id = id;
        this.version = version;
        this.status = status;
        this.found = found;
    }
}
