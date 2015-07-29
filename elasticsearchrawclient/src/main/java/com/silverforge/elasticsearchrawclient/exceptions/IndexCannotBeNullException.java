package com.silverforge.elasticsearchrawclient.exceptions;

public class IndexCannotBeNullException extends Exception {

    @Override
    public String getMessage() {
        return "Index cannot be NULL. Please define index name to client with ConnectorSettings";
    }
}
