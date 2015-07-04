package com.silverforge.elasticsearchrawclient;

import com.google.java.contract.Requires;
import com.google.java.contract.ThrowEnsures;

import java.net.MalformedURLException;
import java.net.URL;

public class Connector {

	private URL url;
	private final String userName;
	private final String password;

	private static final String STRING_EMPTY = "";

	@Requires("!url.equals(\"\") || url !=null")
	public Connector(String url,
					 String userName,
					 String password) throws MalformedURLException {

		this.url = new URL(url);
		this.userName = userName;
		this.password = password;
	}

	public String get() {
		return STRING_EMPTY;
	}

	public String post(String data) {
		return STRING_EMPTY;
	}

	public String put(String data) {
		return STRING_EMPTY;
	}
}
