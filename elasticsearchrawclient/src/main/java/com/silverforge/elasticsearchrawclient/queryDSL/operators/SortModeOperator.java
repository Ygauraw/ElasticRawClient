package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum SortModeOperator {

    MIN("min"),
    MAX("max"),
    SUM("sum"),
    AVG("avg"),
    MEDIAN("median");

    private String value;

    SortModeOperator(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
