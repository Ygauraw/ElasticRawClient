package com.silverforge.elasticsearchrawclient.Connector;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public interface Connectable {
	String get()
		throws IOException,
				KeyManagementException,
				NoSuchAlgorithmException;

	String post(String data)
		throws IOException,
				KeyManagementException,
				NoSuchAlgorithmException;

	String put(String data)
		throws IOException,
				KeyManagementException,
				NoSuchAlgorithmException;
}
