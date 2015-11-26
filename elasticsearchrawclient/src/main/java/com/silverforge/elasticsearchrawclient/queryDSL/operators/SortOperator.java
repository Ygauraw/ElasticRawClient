package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum SortOperator {

    ASC("asc"),
    DESC("desc");

    private String value;

    SortOperator(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
