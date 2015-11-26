package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum MissingOperator {

    LAST("last"),
    FIRST("first");

    private String value;

    MissingOperator(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
