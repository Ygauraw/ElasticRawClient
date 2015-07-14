package com.sf.elastic.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Builder;

@Builder
public class City {

	@Getter
	private String name;

	public City(
			@JsonProperty("name")
			String name) {
		this.name = name;
	}
}
