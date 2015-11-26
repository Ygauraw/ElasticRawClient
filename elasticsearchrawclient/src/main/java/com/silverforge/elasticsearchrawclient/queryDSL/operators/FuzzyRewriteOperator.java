package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum FuzzyRewriteOperator {

    CONSTANT_SCORE_AUTO("constant_score_auto"),
    SCORING_BOOLEAN("scoring_boolean"),
    CONSTANT_SCORE_BOOLEAN("constant_score_boolean"),
    CONSTANT_SCORE_FILTER("constant_score_filter"),
    TOP_TERMS_N("top_terms_N"),
    TOP_TERMS_BOOST_N("top_terms_boost_N");

    private String fuzzyRewrite;

    FuzzyRewriteOperator(String fuzzyRewrite) {
        this.fuzzyRewrite = fuzzyRewrite;
    }

    @Override
    public String toString() {
        return fuzzyRewrite;
    }
}
