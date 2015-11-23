package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum OptimizeBboxOperator {

    NONE("none"),
    MEMORY("memory"),
    INDEXED("indexed");

    private String value;

    OptimizeBboxOperator(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
