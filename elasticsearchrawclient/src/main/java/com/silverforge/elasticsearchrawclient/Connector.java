package com.silverforge.elasticsearchrawclient;

import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class Connector {

	private URL url;
	private final String userName;
	private final String password;

	private static final String STRING_EMPTY = "";
	private static final int READ_TIMEOUT = 7000;
	private static final int CONNECT_TIMEOUT = 7000;

	public Connector(String url, String userName, String password)
			throws MalformedURLException {

		this.url = new URL(url);
		this.userName = userName;
		this.password = password;
	}

	public String get()
			throws IOException,
			KeyManagementException,
			NoSuchAlgorithmException {

		String httpMethod = HttpMethod.GET.toString();
		String result = invokeToEndpoint(httpMethod);

		return result;
	}

	public String post(String data)
			throws IOException,
			KeyManagementException,
			NoSuchAlgorithmException {

		String httpMethod = HttpMethod.POST.toString();
		String result = invokeToEndpoint(httpMethod, data);

		return result;
	}

	public String put(String data)
			throws IOException,
			KeyManagementException,
			NoSuchAlgorithmException {

		String httpMethod = HttpMethod.PUT.toString();
		String result = invokeToEndpoint(httpMethod, data);

		return result;
	}

	@NonNull
	private String invokeToEndpoint(String httpMethod)
			throws IOException,
			NoSuchAlgorithmException,
			KeyManagementException {

		return invokeToEndpoint(httpMethod, STRING_EMPTY);
	}

	@NonNull
	private String invokeToEndpoint(String httpMethod, String data)
			throws IOException,
			NoSuchAlgorithmException,
			KeyManagementException {

		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		addSSLAuthorizationToConnection(conn);
		setConnectionSettings(conn, httpMethod);
		if (data != null && !data.equals(STRING_EMPTY))
			addDataToConnection(data, conn);

		conn.connect();
		StringBuilder retValue = readInputFromConnection(conn);
		conn.disconnect();
		return retValue.toString();
	}

	@NonNull
	private StringBuilder readInputFromConnection(HttpsURLConnection conn)
			throws IOException {

		// Read InputStream
		InputStream inputStream = conn.getInputStream();
		StringBuilder retValue = new StringBuilder();
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			retValue.append(inputLine);
			retValue.append(System.getProperty("line.separator"));
		}
		return retValue;
	}

	private void addDataToConnection(String data, HttpsURLConnection conn)
			throws IOException {

		// Add any data you wish to post here
		OutputStreamWriter outputWriter = new OutputStreamWriter(conn.getOutputStream());
		outputWriter.write(data);
		outputWriter.flush();
		outputWriter.close();
	}

	private void setConnectionSettings(HttpsURLConnection conn, String httpMethod)
			throws ProtocolException {

		// set Timeout and method
		conn.setReadTimeout(READ_TIMEOUT);
		conn.setConnectTimeout(CONNECT_TIMEOUT);
		conn.setRequestMethod(httpMethod);
		conn.setDoInput(true);
		conn.setDoOutput(true);
	}

	private void addSSLAuthorizationToConnection(HttpsURLConnection conn)
			throws NoSuchAlgorithmException,
			KeyManagementException {

		// Create the SSL connection
		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, null, new java.security.SecureRandom());
		conn.setSSLSocketFactory(sc.getSocketFactory());

		// Use this if you need SSL authentication
		String userPass = userName + ":" + password;
		String basicAuth = "Basic "
			+ Base64.encodeToString(userPass.getBytes(),
									Base64.DEFAULT);
		conn.setRequestProperty("Authorization", basicAuth);
	}
}