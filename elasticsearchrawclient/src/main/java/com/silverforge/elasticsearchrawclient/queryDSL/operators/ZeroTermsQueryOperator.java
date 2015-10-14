package com.silverforge.elasticsearchrawclient.queryDSL.operators;

/**
 * Created by jana on 14/10/15.
 */
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
