package com.silverforge.elasticsearchrawclient.testModel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleCity {

    private String name;

    public SimpleCity(
        @JsonProperty("name")
        String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
