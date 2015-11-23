package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum DistanceTypeOperator {

    SLOPPY_ARC("sloppy_arc"),
    ARC("arc"),
    PLANE("plane");

    private String value;

    DistanceTypeOperator(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
