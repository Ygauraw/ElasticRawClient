package com.silverforge.elasticsearchrawclient.connector;

import com.silverforge.elasticsearchrawclient.exceptions.ServerIsNotAvailableException;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public interface Connectable {
	String head(String path)
			throws IOException,
			KeyManagementException,
			NoSuchAlgorithmException, ServerIsNotAvailableException;

	String get(String path)
			throws IOException,
			KeyManagementException,
			NoSuchAlgorithmException, ServerIsNotAvailableException;

	String post(String path, String data)
			throws IOException,
			KeyManagementException,
			NoSuchAlgorithmException, ServerIsNotAvailableException;

	String put(String path, String data)
			throws IOException,
			KeyManagementException,
			NoSuchAlgorithmException, ServerIsNotAvailableException;

	String delete(String path)
			throws IOException,
			KeyManagementException,
			NoSuchAlgorithmException, ServerIsNotAvailableException;

	String delete(String path, String data)
			throws IOException,
			KeyManagementException,
			NoSuchAlgorithmException, ServerIsNotAvailableException;

	ConnectorSettings getSettings();
}
