package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum MultiValueModeOperator {
    MIN("min"),
    MAX("max"),
    AVG("avg"),
    SUM("sum");

    private String value;

    MultiValueModeOperator(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
