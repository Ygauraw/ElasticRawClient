package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum StrategyOperator {

    LEAP_FROG_QUERY_FIRST("leap_frog_query_first"),
    LEAP_FROG_FILTER_FIRST("leap_frog_filter_first"),
    LEAP_FROG("leap_frog"),
    QUERY_FIRST("query_first"),
    RANDOM_ACCESS_N("random_access_N"),
    RANDOM_ACCESS_ALWAYS("random_access_always");

    private String value;

    StrategyOperator(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
