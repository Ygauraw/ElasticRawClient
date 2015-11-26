package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum LogicOperator {

    OR("or"),
    AND("and");

    private String operator;

    LogicOperator(String operator) {

        this.operator = operator;
    }

    @Override
    public String toString() {
        return operator;
    }
}
