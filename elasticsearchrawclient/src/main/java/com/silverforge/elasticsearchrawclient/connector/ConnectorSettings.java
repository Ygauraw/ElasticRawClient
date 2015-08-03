package com.silverforge.elasticsearchrawclient.connector;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The ConnectorSettings contains all the necessary info to connect to the ElasticSearch server(s)
 * Use the <strong>.builder()</strong> to setup the ConnectorSettings properly
 *<strong><pre>
 *ConnectorSettings settings = ConnectorSettings
 *    .builder()
 *    .baseUrl(ELASTIC_URL)
 *    .indices(ELASTIC_INDICES)
 *    .types(ELASTIC_TYPES)
 *    .userName(ELASTIC_APIKEY)
 *    .build();
 *
 *    try {
 *        client = new ElasticClient(settings);
 *    } catch (URISyntaxException e) {
 *        e.printStackTrace();
 *        Log.e(TAG, e.getMessage());
 *        fail(e.getMessage());
 *    }
 *</pre></strong>
 */
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
