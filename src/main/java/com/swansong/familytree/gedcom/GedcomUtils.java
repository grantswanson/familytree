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
            data = data.trim();
            if (tag.length() + data.length() <= MAX_LINE_LENGTH) {
                return String.format(tag, data);
            } else {
                String retStr = "";
                String[] notes = StringUtils.splitStringAlongWords(data, MAX_LINE_LENGTH - tag.length());
                retStr += String.format(tag, notes[0]);

                for (int i = 1; i < notes.length; i++) {
                    if (tag.length() + notes[i].length() <= MAX_LINE_LENGTH) {
                        retStr += String.format("2 CONC %s\n", notes[i]);
                        //System.out.println(" Long String. Adding: "+String.format("2 CONC %s\n", notes[i]));
                    } else {
                        throw new RuntimeException("Line too long!!! Max:" + MAX_LINE_LENGTH +
                                " tag length:" + tag.length() + " data length:" + notes[i].length() +
                                "\n line:'" + String.format(tag, notes[i]) + "'");
                    }
                }
                return retStr;
            }
        }
        return "";
    }
}
