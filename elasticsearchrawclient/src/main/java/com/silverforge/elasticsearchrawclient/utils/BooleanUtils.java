package com.silverforge.elasticsearchrawclient.utils;

public class BooleanUtils {

    public final static String booleanValue(Boolean value) {
        String valueString = "false";
        if (value)
            valueString = "true";

        return valueString;
    }

}
