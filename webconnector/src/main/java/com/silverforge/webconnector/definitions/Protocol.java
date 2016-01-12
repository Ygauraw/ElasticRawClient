package com.silverforge.webconnector.definitions;

public enum Protocol {
    NONE(""),
    HTTP("http"),
    HTTPS("https");

    private String protocol;

    Protocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return protocol;
    }
}
