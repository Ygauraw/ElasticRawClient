package com.silverforge.elasticsearchrawclient.testModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.silverforge.elasticsearchrawclient.model.GeoPoint;

import lombok.Getter;

public class City {

    @Getter private String name;
    @Getter private String description;
    @Getter private Integer population;
    @Getter private Integer settled;
    @Getter private GeoPoint location;

    public City(
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("population") Integer population,
            @JsonProperty("settled") Integer settled,
            @JsonProperty("location") Float[] location) {

        this.name = name;
        this.description = description;
        this.population = population;
        this.settled = settled;

        if (location != null && location.length > 1) {
            this.location = GeoPoint.builder().longitude(location[0]).latitude(location[1]).build();
        }
    }
}
