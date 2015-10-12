package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum FuzzinessOperator {

    AUTO("auto"),
    _0("0"),
    _1("1"),
    _2("2");

    private String value;

    FuzzinessOperator(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
