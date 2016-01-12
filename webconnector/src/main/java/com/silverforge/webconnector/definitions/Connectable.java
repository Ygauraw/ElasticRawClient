package com.silverforge.webconnector.definitions;

import com.silverforge.webconnector.model.ConnectorSettings;
import com.silverforge.webconnector.model.InvokeBinaryResult;
import com.silverforge.webconnector.model.InvokeStringResult;

import rx.Observable;

public interface Connectable {
    InvokeBinaryResult readBinaryContent(String path);
    InvokeStringResult head(String path);
    InvokeStringResult get(String path);
    InvokeStringResult post(String path, String data);
    InvokeStringResult put(String path, String data);
    InvokeStringResult delete(String path);
    InvokeStringResult delete(String path, String data);

    Observable<byte[]> readBinaryContentAsync(String path);
    Observable<String> headAsync(String path);
    Observable<String> getAsync(String path);
    Observable<String> postAsync(String path, String data);
    Observable<String> putAsync(String path, String data);
    Observable<String> deleteAsync(String path);
    Observable<String> deleteAsync(String path, String data);
}
