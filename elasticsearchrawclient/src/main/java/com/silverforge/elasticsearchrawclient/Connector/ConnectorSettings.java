package com.silverforge.elasticsearchrawclient.Connector;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Builder
public class ConnectorSettings {

	@Getter(AccessLevel.MODULE)
	private String url;

	@Getter(AccessLevel.MODULE)
	private String userName;

	@Getter(AccessLevel.MODULE)
	private String password;

	@Getter(AccessLevel.MODULE)
	private int readTimeout;

	@Getter(AccessLevel.MODULE)
	private int connectTimeout;

}
