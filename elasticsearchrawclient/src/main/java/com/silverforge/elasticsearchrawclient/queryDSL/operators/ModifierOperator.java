package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum ModifierOperator {

    LOG("log"),
    LOG1P("log1p"),
    LOG2P("log2p"),
    LN("ln"),
    LN1P("ln1p"),
    LN2P("ln2p"),
    SQUARE("square"),
    SQRT("sqrt"),
    RECIPROCAL("reciprocal");

    private String value;

    ModifierOperator(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }


}
