package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum PhraseTypeOperator {

    PHRASE("phrase"),
    PHRASE_PREFIX("phrase_prefix");

    private String value;

    PhraseTypeOperator(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
