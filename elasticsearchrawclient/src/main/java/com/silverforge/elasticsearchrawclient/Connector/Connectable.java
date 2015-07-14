package com.silverforge.elasticsearchrawclient.Connector;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public interface Connectable {
	String get(String path)
		throws IOException,
				KeyManagementException,
				NoSuchAlgorithmException;

	String post(String path, String data)
		throws IOException,
				KeyManagementException,
				NoSuchAlgorithmException;

	String put(String path, String data)
		throws IOException,
				KeyManagementException,
				NoSuchAlgorithmException;

	String delete(String path, String data)
		throws IOException,
				KeyManagementException,
				NoSuchAlgorithmException;

	ConnectorSettings getSettings();
}
