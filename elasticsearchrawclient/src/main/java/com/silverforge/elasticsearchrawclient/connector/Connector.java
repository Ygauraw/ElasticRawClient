package com.silverforge.elasticsearchrawclient.connector;

import android.text.TextUtils;
import android.util.Base64;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.google.common.base.Predicates;
import com.silverforge.elasticsearchrawclient.elasticFacade.model.InvokeResult;
import com.silverforge.elasticsearchrawclient.exceptions.ServerIsNotAvailableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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
	public InvokeResult head(String path) {
		String httpMethod = HttpMethod.HEAD.toString();
		return invokeToEndpoint(httpMethod, path);
	}

	@Override
	public InvokeResult get(String path) {
		String httpMethod = HttpMethod.GET.toString();
		return invokeToEndpoint(httpMethod, path);
	}

	@Override
	public InvokeResult post(String path, String data) {
		String httpMethod = HttpMethod.POST.toString();
		return invokeToEndpoint(httpMethod, path, data);
	}

	@Override
	public InvokeResult put(String path, String data) {
		String httpMethod = HttpMethod.PUT.toString();
		return invokeToEndpoint(httpMethod, path, data);
	}

	@Override
	public InvokeResult delete(String path) {
		return delete(path, STRING_EMPTY);
	}

	@Override
	public InvokeResult delete(String path, String data) {
		String httpMethod = HttpMethod.DELETE.toString();
		return invokeToEndpoint(httpMethod, path, data);
	}

	private InvokeResult invokeToEndpoint(String httpMethod, String path) {
		return invokeToEndpoint(httpMethod, path, STRING_EMPTY);
	}

	private InvokeResult invokeToEndpoint(String httpMethod, String path, String data) {
		InvokeResult returnResult = new InvokeResult();
		returnResult.setSuccess(true);

		// Build up HttpsUrlConnection
		URL url;
		HttpsURLConnection conn;
		try {
			url = baseUrl.resolve(path).toURL();
			conn = (HttpsURLConnection) url.openConnection();
			addSSLAuthorizationToConnection(conn);
			setConnectionSettings(conn, httpMethod);
		} catch (MalformedURLException e) {
			return invokeExceptionResult(returnResult, e);
		} catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
			return invokeExceptionResult(returnResult, e);
		}

		// Add data to connection if any
		if (data != null && !data.equals(STRING_EMPTY)) {
			try {
				addDataToConnection(data, conn);
			} catch (IOException e) {
				return invokeExceptionResult(returnResult, e);
			}
		}

		// Retrieves with connection status code
		int code;
		String statusCode;
		try {
			code = conn.getResponseCode();
			statusCode = Integer.toString(code);
			if (statusCode.startsWith("4") || statusCode.startsWith("5")) {
				throw new ServerIsNotAvailableException(statusCode);
			}
		} catch (IOException e) {
			return invokeExceptionResult(returnResult, e);
		} catch (ServerIsNotAvailableException e) {
			return invokeExceptionResult(returnResult, e);
		}
		returnResult.setStatusCode(statusCode);

		// Retry pattern for 3 times invoke if read input fails
		Callable<StringBuilder> callable = () -> readInputFromConnection(conn);
		Retryer<StringBuilder> retryer = RetryerBuilder.<StringBuilder>newBuilder()
			.retryIfResult(Predicates.<StringBuilder>isNull())
			.retryIfExceptionOfType(IOException.class)
			.retryIfRuntimeException()
			.withWaitStrategy(WaitStrategies.fixedWait(500, TimeUnit.MILLISECONDS))
			.withStopStrategy(StopStrategies.stopAfterAttempt(3))
			.build();

		// Reads the response from connection
		try {
			StringBuilder returnValue = retryer.call(callable);
			returnResult.setResult(returnValue.toString());
		} catch (ExecutionException | RetryException e) {
			return invokeExceptionResult(returnResult, e);
		}

		return returnResult;
	}

	private InvokeResult invokeExceptionResult(InvokeResult returnResult, Exception e) {
		e.printStackTrace();
		returnResult.addExceptionToResult(e);
		returnResult.setSuccess(false);
		return returnResult;
	}

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

		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Cache-Control", "no-cache");
		conn.setRequestProperty("Content-Length", Integer.toString(data.getBytes().length));

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
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
	}

	private void addSSLAuthorizationToConnection(HttpsURLConnection conn)
			throws NoSuchAlgorithmException, KeyManagementException {

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
			// [known issue in jre] : http://mail-archives.apache.org/mod_mbox/maven-users/201103.mbox/%3CAANLkTikrKWesVvAHxntLVSNoRw=DZRO=SBhsvqUJ0zHy@mail.gmail.com%3E
			conn.setRequestProperty("Authorization", basicAuth.replace("\n", ""));
		}
	}
}