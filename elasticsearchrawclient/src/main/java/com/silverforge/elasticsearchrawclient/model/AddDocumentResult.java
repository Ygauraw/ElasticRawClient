package com.silverforge.elasticsearchrawclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

public class AddDocumentResult {

    @Getter
    private String index;

    @Getter
    private String type;

    @Getter
    private String id;

    @Getter
    private Integer version;

    @Getter
    private Boolean created;

    public AddDocumentResult(
        @JsonProperty("_index")
        String index,
        @JsonProperty("_type")
        String type,
        @JsonProperty("_id")
        String id,
        @JsonProperty("_version")
        Integer version,
        @JsonProperty("created")
        Boolean created) {

        this.index = index;
        this.type = type;
        this.id = id;
        this.version = version;
        this.created = created;
    }
}
