package com.silverforge.elasticsearchrawclient.testModel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class City {

    private String name;

    public City(
            @JsonProperty("name")
            String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
