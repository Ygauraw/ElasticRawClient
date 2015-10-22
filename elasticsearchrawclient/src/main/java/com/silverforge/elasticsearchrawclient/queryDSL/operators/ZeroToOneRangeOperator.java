package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum ZeroToOneRangeOperator {

    _0_0("0.0"),
    _0_1("0.1"),
    _0_2("0.2"),
    _0_3("0.3"),
    _0_4("0.4"),
    _0_5("0.5"),
    _0_6("0.6"),
    _0_7("0.7"),
    _0_8("0.8"),
    _0_9("0.9"),
    _1_0("1.0");

    private String value;

    ZeroToOneRangeOperator(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
