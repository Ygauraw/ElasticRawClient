package com.silverforge.elasticsearchrawclient.Connector;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class Connector implements Connectable {
	private static final String STRING_EMPTY = "";
	private static final String DEFAULT_BASE_URL = "http://localhost:9200";
	private static final int DEFAULT_TIMEOUT = 7000;

	private URI baseUrl;
	private String userName = STRING_EMPTY;
	private String password = STRING_EMPTY;
	private int readTimeout = 7000;
	private int connectTimeout = 7000;
	private ConnectorSettings settings;

	public Connector(ConnectorSettings settings)
		throws URISyntaxException {
		this.settings = settings;

		if (settings != null) {
			if (!TextUtils.isEmpty(settings.getBaseUrl()))
				baseUrl = new URI(settings.getBaseUrl());
			else {
				baseUrl = new URI(DEFAULT_BASE_URL);
				this.settings.setBaseUrl(DEFAULT_BASE_URL);
			}

			if (!TextUtils.isEmpty(settings.getUserName()))
				userName = settings.getUserName();
			else
				this.settings.setUserName(STRING_EMPTY);

			if (!TextUtils.isEmpty(settings.getPassword()))
				password = settings.getPassword();
			else
				this.settings.setPassword(STRING_EMPTY);

			if (settings.getIndices() == null)
				this.settings.setIndices(new String[0]);

			if (settings.getTypes() == null)
				this.settings.setTypes(new String[0]);

			if (settings.getReadTimeout() > 1000)
				readTimeout = settings.getReadTimeout();
			else
				this.settings.setReadTimeout(DEFAULT_TIMEOUT);

			if (settings.getConnectTimeout() > 1000)
				connectTimeout = settings.getConnectTimeout();
			else
				this.settings.setConnectTimeout(DEFAULT_TIMEOUT);
		}
	}

	public ConnectorSettings getSettings() {
		return settings;
	}

	@Override
	public String get(String path)
			throws IOException,
			KeyManagementException,
			NoSuchAlgorithmException {

		String httpMethod = HttpMethod.GET.toString();
		String result = invokeToEndpoint(httpMethod, path);

		return result;
	}

	@Override
	public String post(String path, String data)
			throws IOException,
			KeyManagementException,
			NoSuchAlgorithmException {

		String httpMethod = HttpMethod.POST.toString();
		String result = invokeToEndpoint(httpMethod, path, data);

		return result;
	}

	@Override
	public String put(String path, String data)
			throws IOException,
			KeyManagementException,
			NoSuchAlgorithmException {

		String httpMethod = HttpMethod.PUT.toString();
		String result = invokeToEndpoint(httpMethod, path, data);

		return result;
	}

	@Override
	public String delete(String path, String data)
			throws IOException,
			KeyManagementException,
			NoSuchAlgorithmException {


		String httpMethod = HttpMethod.DELETE.toString();
		String result = invokeToEndpoint(httpMethod, path, data);

		return result;
	}

	@NonNull
	private String invokeToEndpoint(String httpMethod, String path)
			throws IOException,
			NoSuchAlgorithmException,
			KeyManagementException {

		return invokeToEndpoint(httpMethod, path, STRING_EMPTY);
	}

	@NonNull
	private String invokeToEndpoint(String httpMethod, String path, String data)
			throws IOException,
			NoSuchAlgorithmException,
			KeyManagementException {

		URL url = baseUrl.resolve(path).toURL();

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