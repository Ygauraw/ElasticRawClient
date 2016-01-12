package com.silverforge.webconnector.utils;

import android.text.TextUtils;

public final class ParameterCheck {

    public static String ensureValue(String value) {
        return ensureValue(value, null);
    }

    public static String ensureValue(String value, String defaultValue) {
        String retValue = "";

        if (!TextUtils.isEmpty(value))
            retValue = value;
        else if (!TextUtils.isEmpty(defaultValue))
            retValue = defaultValue;

        return retValue;
    }

    public static String[] ensureValue(String[] value) {
        return ensureValue(value, null);
    }

    public static String[] ensureValue(String[] value, String[] defaultValue) {
        String[] retValue = new String[] {};

        if (value != null)
            retValue = value;
        else if (defaultValue != null)
            retValue = defaultValue;

        return retValue;
    }

    public static int ensureValue(int value, int defaultValue, int minThreshold, int maxThreshold) {
        int retValue = 0;

        if (value >= minThreshold && value <= maxThreshold)
            retValue = value;
        else if (defaultValue >= minThreshold && defaultValue <= maxThreshold)
            retValue = defaultValue;

        return retValue;
    }
}
