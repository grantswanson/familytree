package com.swansong.familytree.model;

import com.swansong.familytree.StringUtilities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class Name {

    private String surName="";
    private String firstNames="";
    private String nickName="";
    private String suffix="";

    private String marriedName ="";

//    private String prefix;
//    private String title;
    public static Name parseLastCommaFirstName(String str) {
        String[] names = str.split(",");
        if (names.length == 0) {
            throw new RuntimeException("Unexpected lastName, firstName format: It is blank" + str);
        } else if (names.length >3) {
            throw new RuntimeException("Unexpected lastName, firstName format: It contains an extra comma: '" + str + "'");
        }
        Name name = new Name();
        name.setSurName(toNameCase(names[0].trim()));
        if (names.length >= 2) {
            String n = toNameCase(names[1]);
            name.setFirstNames(extractFirstNames(n).trim());
            name.setNickName(extractNickName(n).trim());
            name.setMarriedName(extractMarriedName(n).trim());
        }
        if (names.length == 3) {
            name.setSuffix(toNameCase(names[2]).trim());

        }
        return name;
    }

    private static String extractNickName(String s) {
        return extractTextBetween(s, "\"","\"");
    }
    private static String extractMarriedName(String s) {
        return extractTextBetween(s, "[","]");
    }
    private static String extractTextBetween(String s, String begin, String end) {
        int firstQuoteIndex = s.indexOf(begin);
        int secondQuoteIndex = s.indexOf(end, firstQuoteIndex + 1);
        if (firstQuoteIndex != -1 && secondQuoteIndex != -1) {
            return s.substring(firstQuoteIndex + 1, secondQuoteIndex);
        }
        return "";
    }
    private static String removeTextBetween(String s, String begin, String end) {
        int firstQuoteIndex = s.indexOf(begin);
        int secondQuoteIndex = s.indexOf(end, firstQuoteIndex + 1);
        if (firstQuoteIndex != -1 && secondQuoteIndex != -1) {
            return s.substring(0, firstQuoteIndex) + s.substring(secondQuoteIndex + 1).trim();
        }
        return s;
    }
    private static String extractFirstNames(String s) {
        return removeTextBetween(                           // remove marriedName
                removeTextBetween(s, "\"", "\""), // remove nickname
                "[","]");                              // remove marriedName
    }

    private static String toNameCase(String s) {
        // only change the case if already in all caps
        if(StringUtilities.isAllCaps(s) ) {
            return StringUtilities.toCapitalizedCase(s);
        }
        // else leave in existing case. E.g. McGee should NOT be Mcgee
        return s;
    }

    public String getLastCommaFirst() {
        String key =  surName+", "+firstNames;
        if (nickName!= null && !nickName.isEmpty()) key+= " \""+nickName+"\"";
        if (marriedName!=null && !marriedName.isEmpty()) key+= " ["+marriedName+"]";
        if (suffix!=null && !suffix.isEmpty()) key+= ", "+suffix;
        return key;
    }
}