package com.silverforge.elasticsearchrawclient.queryDSL.operators;

public enum TimeZoneOperator {

    UTC("+00:00"),
    UTC1("+01:00"),
    UTC2("+02:00"),
    UTC3("+03:00"),
    UTC4("+04:00"),
    UTC5("+05:00"),
    UTC6("+06:00"),
    UTC7("+07:00"),
    UTC8("+08:00"),
    UTC9("+09:00"),
    UTC10("+10:00"),
    UTC11("+11:00"),
    UTC12("+12:00"),

    UTC_1("-01:00"),
    UTC_2("-02:00"),
    UTC_3("-03:00"),
    UTC_4("-04:00"),
    UTC_5("-05:00"),
    UTC_6("-06:00"),
    UTC_7("-07:00"),
    UTC_8("-08:00"),
    UTC_9("-09:00"),
    UTC_10("-10:00"),
    UTC_11("-11:00");

    private String value;

    TimeZoneOperator(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
