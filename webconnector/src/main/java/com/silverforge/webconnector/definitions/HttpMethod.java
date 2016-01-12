package com.silverforge.webconnector.definitions;

public enum HttpMethod {

    POST("POST"),
    GET("GET"),
    PUT("PUT"),
    DELETE("DELETE"),
    HEAD("HEAD");

    private String methodName;

    HttpMethod(String methodName) {
        this.methodName = methodName;

    }

    @Override
    public String toString() {
        return methodName;
    }
}
