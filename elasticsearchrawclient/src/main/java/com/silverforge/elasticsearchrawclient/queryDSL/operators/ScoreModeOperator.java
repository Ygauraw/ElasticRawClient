package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum ScoreModeOperator {

    NONE("none"),
    MULTIPLY("multiply"),
    SUM("sum"),
    AVG("avg"),
    FIRST("first"),
    MAX("max"),
    MIN("min");

    private String value;

    ScoreModeOperator(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
