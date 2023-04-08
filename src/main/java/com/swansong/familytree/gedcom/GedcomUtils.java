package com.swansong.familytree.gedcom;

import com.swansong.familytree.utils.StringUtils;

public class GedcomUtils {
    private static final int MAX_LINE_LENGTH = 255;

    public static String getDateAndPlace(String str1, String date, String place) {
        String record = "";
        if (!StringUtils.isNullOrBlank(date) || !StringUtils.isNullOrBlank(place)) {
            record += str1;
            record += getIfNotNullOrBlank("2 DATE %s\n", date);
            record += getIfNotNullOrBlank("2 PLAC %s\n", place);
        }
        return record;
    }

    public static String getIfNotNullOrBlank(String tag, String data) {
        if (!StringUtils.isNullOrBlank(data)) {
            if (tag.length() + data.length() <= MAX_LINE_LENGTH) {
                return String.format(tag, data);
            } else {
                throw new RuntimeException("Line too long!!! Max:" + MAX_LINE_LENGTH +
                        " length:" + tag.length() + data.length() +
                        "\n line:" + String.format(tag, data));
            }
        }
        return "";
    }
}
