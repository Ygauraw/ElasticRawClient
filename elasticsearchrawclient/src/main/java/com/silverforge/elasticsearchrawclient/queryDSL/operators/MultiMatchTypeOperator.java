package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum MultiMatchTypeOperator {

    BEST_FIELDS("best_fields"),
    MOST_FIELDS("most_fields"),
    CROSS_FIELDS("cross_fields"),
    PHRASE("phrase"),
    PHRASE_PREFIX("phrase_prefix");

    private String value;

    MultiMatchTypeOperator(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
