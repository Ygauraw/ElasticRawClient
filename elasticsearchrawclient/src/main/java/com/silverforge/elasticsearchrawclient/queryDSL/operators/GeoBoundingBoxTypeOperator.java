package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum GeoBoundingBoxTypeOperator {

    MEMORY("memory"),
    INDEXED("indexed");

    private String value;

    GeoBoundingBoxTypeOperator(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
