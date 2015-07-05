package com.silverforge.elasticsearchrawclient.Connector;

public enum HttpMethod {

	POST("POST"),
	GET("GET"),
	PUT("PUT");

	private String methodName;

	HttpMethod(String methodName) {

		this.methodName = methodName;
	}

	@Override
	public String toString() {
		return methodName;
	}
}
