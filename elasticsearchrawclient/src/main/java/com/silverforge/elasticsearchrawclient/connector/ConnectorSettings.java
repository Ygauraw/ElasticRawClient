package com.silverforge.elasticsearchrawclient.connector;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class ConnectorSettings {

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.MODULE)
	private String baseUrl;

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.MODULE)
	private String userName;

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.MODULE)
	private String password;

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.MODULE)
	private String[] indices;

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.MODULE)
	private String[] types;

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.MODULE)
	private int readTimeout;

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.MODULE)
	private int connectTimeout;
}
