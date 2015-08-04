package com.silverforge.elasticsearchrawclient.exceptions;

public class TypeCannotBeNullException extends Exception {

    @Override
    public String getMessage() {
        return "Type cannot be NULL. Please define type name to client via ConnectorSettings";
    }
}
