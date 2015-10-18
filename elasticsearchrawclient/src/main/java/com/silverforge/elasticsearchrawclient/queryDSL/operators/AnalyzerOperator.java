package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum AnalyzerOperator {

    NOT_ANALYZED("not_analyzed"),
    STANDARD("standard"),
    SIMPLE("simple"),
    WHITESPACE("whitespace"),
    STOP("stop"),
    KEYWORD("keyword"),
    PATTERN("pattern"),
    SNOWBALL("snowball"),

    ARABIC("arabic"),
    ARMENIAN("armenian"),
    BASQUE("basque"),
    BRAZILIAN("brazilian"),
    BULGARIAN("bulgarian"),
    CATALAN("catalan"),
    CHINESE("chinese"),
    CJK("cjk"),
    CZECH("czech"),
    DANISH("danish"),
    DUTCH("dutch"),
    ENGLISH("english"),
    FINNISH("finnish"),
    FRENCH("french"),
    GALICIAN("galician"),
    GERMAN("german"),
    GREEK("greek"),
    HINDI("hindi"),
    HUNGARIAN("hungarian"),
    INDONESIAN("indonesian"),
    IRISH("irish"),
    ITALIAN("italian"),
    LATVIAN("latvian"),
    NORWEGIAN("norwegian"),
    PERSIAN("persian"),
    PORTUGUESE("portuguese"),
    ROMANIAN("romanian"),
    RUSSIAN("russian"),
    SORANI("sorani"),
    SPANISH("spanish"),
    SWEDISH("swedish"),
    TURKISH("turkish"),
    THAI("thai");

    private String value;

    AnalyzerOperator(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
