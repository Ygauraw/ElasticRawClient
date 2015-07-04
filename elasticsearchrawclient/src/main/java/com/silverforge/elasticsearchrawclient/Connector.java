package com.silverforge.elasticsearchrawclient;

import java.net.MalformedURLException;
import java.net.URL;

public class Connector {

	private URL url;
	private final String userName;
	private final String password;

	private static final String STRING_EMPTY = "";


	/*
		@pre url != ""
	 */
	public Connector(String url,
					 String userName,
					 String password) {

		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			try {
				this.url = new URL("localhost:9200");
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
		}

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
