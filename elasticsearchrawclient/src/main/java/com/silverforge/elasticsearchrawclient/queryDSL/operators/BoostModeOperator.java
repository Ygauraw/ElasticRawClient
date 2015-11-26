package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum BoostModeOperator {

    MULTIPLY("multiply"),
    REPLACE("replace"),
    SUM("sum"),
    AVG("avg"),
    MAX("max"),
    MIN("min");

    private String value;

    BoostModeOperator(String value) {
            this.value = value;
    }

    @Override
    public String toString() {
            return value;
    }
}
