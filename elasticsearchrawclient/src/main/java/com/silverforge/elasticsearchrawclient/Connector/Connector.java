package com.silverforge.elasticsearchrawclient.Connector;

import android.support.annotation.NonNull;
import android.text.TextUtils;
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

	private static final String STRING_EMPTY = "";

	private URL url = new URL("http://localhost:9200");
	private String userName = STRING_EMPTY;
	private String password = STRING_EMPTY;
	private int readTimeout = 7000;
	private int connectTimeout = 7000;

	public Connector(ConnectorSettings settings)
			throws MalformedURLException {

		if (settings != null) {
			if (!TextUtils.isEmpty(settings.getUrl()))
				this.url = new URL(settings.getUrl());

			if (!TextUtils.isEmpty(settings.getUserName()))
				this.userName = settings.getUserName();

			if (!TextUtils.isEmpty(settings.getPassword()))
				this.password = settings.getPassword();

			if (settings.getReadTimeout() > 1000)
				this.readTimeout = settings.getReadTimeout();

			if (settings.getConnectTimeout() > 1000)
				this.connectTimeout = settings.getConnectTimeout();
		}
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
		conn.setReadTimeout(readTimeout);
		conn.setConnectTimeout(connectTimeout);
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
		if (!TextUtils.isEmpty(userName) || !TextUtils.isEmpty(password)) {
			String userPass = userName + ":" + password;
			String basicAuth = "Basic "
				+ Base64.encodeToString(userPass.getBytes(),
				Base64.DEFAULT);
			conn.setRequestProperty("Authorization", basicAuth);
		}
	}
}