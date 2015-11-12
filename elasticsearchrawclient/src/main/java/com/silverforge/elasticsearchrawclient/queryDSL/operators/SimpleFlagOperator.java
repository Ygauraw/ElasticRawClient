package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum SimpleFlagOperator {

    ALL("ALL"),
    AND("AND"),
    OR("OR"),
    NOT("NOT"),
    PREFIX("PREFIX"),
    PHRASE("PHRASE"),
    PRECEDENCE("PRECEDENCE"),
    ESCAPE("ESCAPE"),
    WHITESPACE("WHITESPACE"),
    FUZZY("FUZZY"),
    NEAR("NEAR"),
    SLOP("SLOP"),
    NONE("NONE");

    private String value;

    SimpleFlagOperator(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
