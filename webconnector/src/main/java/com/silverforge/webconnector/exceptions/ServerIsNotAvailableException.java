package com.silverforge.webconnector.exceptions;

public class ServerIsNotAvailableException extends Exception {
    private String statusCode;

    public ServerIsNotAvailableException(String statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return String.format("Server response code : %s", statusCode);
    }
}
