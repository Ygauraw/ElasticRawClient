package com.silverforge.webconnector.model;

import java.net.URI;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Builder;

@Data
public class ConnectorSettings {
    private URI uri;

    private String baseUrl;
    private String userName;
    private String password;

    private int readTimeout;
    private int connectTimeout;
    private int retryCount;

    @Builder
    public ConnectorSettings(
            String baseUrl,
            String userName,
            String password,
            int readTimeout,
            int connectTimeout,
            int retryCount) {

        this.baseUrl = baseUrl;
        this.userName = userName;
        this.password = password;
        this.readTimeout = readTimeout;
        this.connectTimeout = connectTimeout;
        this.retryCount = retryCount;
    }
}
