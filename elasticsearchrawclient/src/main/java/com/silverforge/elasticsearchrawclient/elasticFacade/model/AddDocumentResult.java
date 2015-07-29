package com.silverforge.elasticsearchrawclient.elasticFacade.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddDocumentResult {
    private String index;
    private String type;
    private String id;
    private Integer version;
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

    public String getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public Boolean getCreated() {
        return created;
    }
}
