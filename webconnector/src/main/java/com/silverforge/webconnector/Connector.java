package com.silverforge.webconnector;

import android.text.TextUtils;
import android.util.Base64;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.google.common.base.Predicates;
import com.silverforge.webconnector.exceptions.ServerIsNotAvailableException;
import com.silverforge.webconnector.exceptions.SettingsIsNullException;
import com.silverforge.webconnector.model.ConnectorSettings;
import com.silverforge.webconnector.model.InvokeBinaryResult;
import com.silverforge.webconnector.model.InvokeResult;
import com.silverforge.webconnector.model.InvokeStringResult;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
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

import rx.Observable;

import static com.silverforge.webconnector.utils.ParameterCheck.ensureValue;

class Connector {
    private static final int RETRY_WAIT_TIME = 500;
    private static final int BYTE_CHUNK_SIZE = 1024;
    private static final String DEFAULT_BASE_URL = "http://localhost";
    private static final int DEFAULT_TIMEOUT = 7000;
    private static final int DEFAULT_RETRY_COUNT = 3;


    protected static final String STRING_EMPTY = "";
    protected ConnectorSettings connectorSettings;


    public Connector(ConnectorSettings settings)
            throws SettingsIsNullException, URISyntaxException {

        prepareConnectorSettings(settings);
    }

    public ConnectorSettings getSettings() {
        return connectorSettings;
    }

    public void setSettings(ConnectorSettings settings)
            throws SettingsIsNullException, URISyntaxException {
        prepareConnectorSettings(settings);
    }

    // region Async methods

    protected Observable<String> invokeToEndpointAsync(String httpMethod, String path) {
        return invokeToEndpointAsync(httpMethod, path, STRING_EMPTY);
    }

    protected Observable<String> invokeToEndpointAsync(String httpMethod, String path, String data) {
        return Observable.create(subscriber -> {
            InvokeStringResult result = invokeToEndpoint(httpMethod, path, data);

            if (result.isSuccess()) {
                subscriber.onNext(result.getResult());
            } else {
                Observable
                        .from(result.getAggregatedExceptions())
                        .subscribe(e -> subscriber.onError(e));
            }
            subscriber.onCompleted();
        });
    }

    protected Observable<byte[]> invokeToBinaryEndpointAsync(String httpMethod, String path) {
        return invokeToBinaryEndpointAsync(httpMethod, path, BYTE_CHUNK_SIZE);
    }

    protected Observable<byte[]> invokeToBinaryEndpointAsync(String httpMethod, String path, int chunkSize) {
        return Observable.create(subscriber -> {
            try {
                URL url = connectorSettings
                        .getUri()
                        .resolve(path)
                        .toURL();
                HttpURLConnection conn = getConnection(url, httpMethod);

                InputStream inputStream = new BufferedInputStream(conn.getURL().openStream(), 8192);
                byte data[] = new byte[chunkSize];
                while (inputStream.read(data) != -1) {
                    subscriber.onNext(data);
                }
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(e);
            } finally {
                subscriber.onCompleted();
            }
        });
    }

    // endregion

    // region Sync methods

    protected InvokeStringResult invokeToEndpoint(String httpMethod, String path) {
        return invokeToEndpoint(httpMethod, path, STRING_EMPTY);
    }

    protected InvokeStringResult invokeToEndpoint(String httpMethod, String path, String data) {
        InvokeStringResult returnResult = new InvokeStringResult();
        returnResult.setSuccess(true);

        // Build up Http(s)UrlConnection
        try {
            URL url = connectorSettings
                    .getUri()
                    .resolve(path)
                    .toURL();

            HttpURLConnection conn = getConnection(url, httpMethod);

            // Add data to connection if any
            if (data != null && !data.equals(STRING_EMPTY))
                addDataToConnection(data, conn);

            // Retrieves with connection status code
            String statusCode = getStatusCode(conn);
            returnResult.setStatusCode(statusCode);

            // Retry pattern for x times invoke if read input fails
            final HttpURLConnection finalConn = conn;
            Callable<StringBuilder> callable = () -> readInputFromConnection(finalConn);
            Retryer<StringBuilder> retryer = RetryerBuilder.<StringBuilder>newBuilder()
                    .retryIfResult(Predicates.<StringBuilder>isNull())
                    .retryIfExceptionOfType(IOException.class)
                    .retryIfRuntimeException()
                    .withWaitStrategy(WaitStrategies.fixedWait(RETRY_WAIT_TIME, TimeUnit.MILLISECONDS))
                    .withStopStrategy(StopStrategies.stopAfterAttempt(connectorSettings.getRetryCount()))
                    .build();

            // Reads the response from connection
            StringBuilder returnValue = retryer.call(callable);
            returnResult.setResult(returnValue.toString());

        } catch (ExecutionException | RetryException |IOException | NoSuchAlgorithmException | KeyManagementException | ServerIsNotAvailableException e) {
            return (InvokeStringResult)invokeExceptionResult(returnResult, e);
        }

        return returnResult;
    }

    protected InvokeBinaryResult invokeToBinaryEndpoint(String httpMethod, String path) {
        InvokeBinaryResult returnResult = new InvokeBinaryResult();
        returnResult.setSuccess(true);

        try {
            URL url = connectorSettings
                    .getUri()
                    .resolve(path)
                    .toURL();

            HttpURLConnection conn = getConnection(url, httpMethod);

            // Retrieves with connection status code
            String statusCode = getStatusCode(conn);
            returnResult.setStatusCode(statusCode);

            // Retry pattern for x times invoke if read input fails
            final HttpURLConnection finalConn = conn;
            Callable<byte[]> callable = () -> readBinaryInputFromConnection(finalConn);
            Retryer<byte[]> retryer = RetryerBuilder.<byte[]>newBuilder()
                    .retryIfResult(Predicates.<byte[]>isNull())
                    .retryIfExceptionOfType(IOException.class)
                    .retryIfRuntimeException()
                    .withWaitStrategy(WaitStrategies.fixedWait(RETRY_WAIT_TIME, TimeUnit.MILLISECONDS))
                    .withStopStrategy(StopStrategies.stopAfterAttempt(connectorSettings.getRetryCount()))
                    .build();

            // Reads the response from connection
            byte[] returnValue = retryer.call(callable);
            returnResult.setResult(returnValue);

        } catch (ExecutionException | RetryException |ServerIsNotAvailableException |IOException | NoSuchAlgorithmException | KeyManagementException e) {
            return (InvokeBinaryResult)invokeExceptionResult(returnResult, e);
        }
        return returnResult;
    }

    // endregion

    // region private methods

    private void prepareConnectorSettings(ConnectorSettings settings)
            throws SettingsIsNullException, URISyntaxException {

        if (settings == null)
            throw new SettingsIsNullException();

        connectorSettings = ConnectorSettings
                .builder()
                .baseUrl(ensureValue(settings.getBaseUrl(), DEFAULT_BASE_URL))
                .userName(ensureValue(settings.getUserName()))
                .password(ensureValue(settings.getPassword()))
                .readTimeout(ensureValue(settings.getReadTimeout(), DEFAULT_TIMEOUT, 1000, 10000))
                .connectTimeout(ensureValue(settings.getConnectTimeout(), DEFAULT_TIMEOUT, 1000, 10000))
                .retryCount(ensureValue(settings.getRetryCount(), DEFAULT_RETRY_COUNT, 1, 5))
                .build();

        URI url = new URI(connectorSettings.getBaseUrl());
        connectorSettings.setUri(url);
    }

    private String getStatusCode(HttpURLConnection conn)
            throws IOException, ServerIsNotAvailableException {

        int code;
        String statusCode;
        code = conn.getResponseCode();
        statusCode = Integer.toString(code);
        if (statusCode.startsWith("4") || statusCode.startsWith("5"))
            throw new ServerIsNotAvailableException(statusCode);
        return statusCode;
    }

    private HttpURLConnection getConnection(URL url, String httpMethod)
            throws IOException, KeyManagementException, NoSuchAlgorithmException {

        HttpURLConnection conn = null;
        String protocol = url.getProtocol().toLowerCase();

        if (protocol.equals("http")) {
            conn = (HttpURLConnection) url.openConnection();
            setConnectionSettings(conn, httpMethod);
        } else if (protocol.equals("https")) {
            conn = (HttpsURLConnection) url.openConnection();
            HttpsURLConnection secureConn = (HttpsURLConnection)conn;
            addSSLAuthorizationToConnection(secureConn);
            setConnectionSettings(secureConn, httpMethod);
        }
        return conn;
    }

    private InvokeResult invokeExceptionResult(InvokeResult returnResult, Exception e) {
        e.printStackTrace();
        returnResult.addExceptionToResult(e);
        returnResult.setSuccess(false);
        return returnResult;
    }

    private StringBuilder readInputFromConnection(HttpURLConnection conn)
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

    private byte[] readBinaryInputFromConnection(HttpURLConnection conn)
            throws IOException {

        int count;
        InputStream inputStream = new BufferedInputStream(conn.getURL().openStream(), 8192);
        ByteArrayOutputStream retValue = new ByteArrayOutputStream();
        byte data[] = new byte[1024];
        while ((count = inputStream.read(data)) != -1) {
            retValue.write(data, 0, count);
        }
        retValue.flush();
        retValue.close();
        inputStream.close();

        return retValue.toByteArray();
    }

    private void addDataToConnection(String data, HttpURLConnection conn)
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

    private void setConnectionSettings(HttpURLConnection conn, String httpMethod)
            throws ProtocolException {

        // set Timeout and method
        conn.setReadTimeout(connectorSettings.getReadTimeout());
        conn.setConnectTimeout(connectorSettings.getConnectTimeout());
        conn.setRequestMethod(httpMethod);
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
    }

    private void addSSLAuthorizationToConnection(HttpsURLConnection conn)
            throws NoSuchAlgorithmException, KeyManagementException {

        String userName = connectorSettings.getUserName();
        String password = connectorSettings.getPassword();

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

    // endregion
}