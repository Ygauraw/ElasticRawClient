package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum GeoShapeTypeOperator {
    POINT("point"),
    LINESTRING("linestring"),
    POLYGON("polygon"),
    MULTIPOINT("multipoint"),
    MULTILINESTRING("multilinestring"),
    MULTIPOLYGON("multipolygon"),
    GEOMETRYCOLLECTION("geometrycollection"),
    ENVELOPE("envelope"),
    CIRCLE("circle");

    private String value;

    GeoShapeTypeOperator(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
