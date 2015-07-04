package com.sf.elastic.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Builder
public class City {

	@Getter
	private String name;

	public City(@JsonProperty("name")String name) {
		this.name = name;
	}
}
