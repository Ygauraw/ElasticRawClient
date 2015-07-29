package com.silverforge.elasticsearchrawclient.exceptions;

public class ServerIsNotAvailableException extends Throwable {
    private String statusCode;

    public ServerIsNotAvailableException(String statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return String.format("Server response code : %s", statusCode);
    }
}
