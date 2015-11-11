package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum FlagOperator {

    ALL("ALL"),
    ANYSTRING("ANYSTRING"),
    COMPLEMENT("COMPLEMENT"),
    EMPTY("EMPTY"),
    INTERSECTION("INTERSECTION"),
    INTERVAL("INTERVAL"),
    NONE("NONE");

    private String value;

    FlagOperator(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
