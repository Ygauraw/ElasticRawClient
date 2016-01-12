package com.silverforge.webconnector;

import com.silverforge.webconnector.definitions.Connectable;
import com.silverforge.webconnector.definitions.HttpMethod;
import com.silverforge.webconnector.exceptions.SettingsIsNullException;
import com.silverforge.webconnector.model.ConnectorSettings;
import com.silverforge.webconnector.model.InvokeBinaryResult;
import com.silverforge.webconnector.model.InvokeStringResult;

import java.net.URISyntaxException;

import rx.Observable;

public class EndpointConnector
        extends Connector
        implements Connectable {

    public EndpointConnector(ConnectorSettings settings)
            throws SettingsIsNullException, URISyntaxException {
        super(settings);
    }

    // region Async methods

    @Override
    public Observable<byte[]> readBinaryContentAsync(String path) {
        String httpMethod = HttpMethod.GET.toString();
        return invokeToBinaryEndpointAsync(httpMethod, path);
    }

    @Override
    public Observable<String> headAsync(String path) {
        String httpMethod = HttpMethod.HEAD.toString();
        return invokeToEndpointAsync(httpMethod, path);
    }

    @Override
    public Observable<String> getAsync(String path) {
        String httpMethod = HttpMethod.GET.toString();
        return invokeToEndpointAsync(httpMethod, path);
    }

    @Override
    public Observable<String> postAsync(String path, String data) {
        String httpMethod = HttpMethod.POST.toString();
        return invokeToEndpointAsync(httpMethod, path, data);
    }

    @Override
    public Observable<String> putAsync(String path, String data) {
        String httpMethod = HttpMethod.PUT.toString();
        return invokeToEndpointAsync(httpMethod, path, data);
    }

    @Override
    public Observable<String> deleteAsync(String path) {
        return deleteAsync(path, STRING_EMPTY);
    }

    @Override
    public Observable<String> deleteAsync(String path, String data) {
        String httpMethod = HttpMethod.DELETE.toString();
        return invokeToEndpointAsync(httpMethod, path, data);
    }

    // endregion

    // region Sync methods

    @Override
    public InvokeBinaryResult readBinaryContent(String path) {
        String httpMethod = HttpMethod.GET.toString();
        return invokeToBinaryEndpoint(httpMethod, path);
    }

    @Override
    public InvokeStringResult head(String path) {
        String httpMethod = HttpMethod.HEAD.toString();
        return invokeToEndpoint(httpMethod, path);
    }

    @Override
    public InvokeStringResult get(String path) {
        String httpMethod = HttpMethod.GET.toString();
        return invokeToEndpoint(httpMethod, path);
    }

    @Override
    public InvokeStringResult post(String path, String data) {
        String httpMethod = HttpMethod.POST.toString();
        return invokeToEndpoint(httpMethod, path, data);
    }

    @Override
    public InvokeStringResult put(String path, String data) {
        String httpMethod = HttpMethod.PUT.toString();
        return invokeToEndpoint(httpMethod, path, data);
    }

    @Override
    public InvokeStringResult delete(String path) {
        return delete(path, STRING_EMPTY);
    }

    @Override
    public InvokeStringResult delete(String path, String data) {
        String httpMethod = HttpMethod.DELETE.toString();
        return invokeToEndpoint(httpMethod, path, data);
    }

    // endregion
}
