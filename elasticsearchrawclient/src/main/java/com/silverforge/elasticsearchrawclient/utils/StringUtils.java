package com.silverforge.elasticsearchrawclient.utils;

import android.text.TextUtils;

import java.util.UUID;

public class StringUtils {
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static String makeCommaSeparatedList(String[] list) {
        if (list == null || list.length == 0)
            return "";

        if (list.length == 1)
            return list[0];

        return TextUtils.join(",", list);
    }

    public static String makeCommaSeparatedListWithQuotationMark(String[] list) {
        if (list == null || list.length == 0)
            return "";

        if (list.length == 1)
            return "\"" + list[0] + "\"" ;

        for (int i = 0; i < list.length; i++) {
            list[i] = "\"" + list[i] + "\"" ;
        }

        return TextUtils.join(",", list);
    }

    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String ensurePath(String path) {
        if (path.startsWith("/"))
            return path;

        return String.format("/%s", path);
    }
}
