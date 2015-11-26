package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum ZeroTermsQueryOperator {

    NONE("none"),
    ALL("all");

    private String zeroTerms;

    ZeroTermsQueryOperator(String zeroTerms) {
        this.zeroTerms = zeroTerms;
    }

    @Override
    public String toString() {
        return zeroTerms;
    }
}
