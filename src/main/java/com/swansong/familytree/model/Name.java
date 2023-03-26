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

    private String surName = "";
    private String firstNames = "";
    private String nickName = "";
    private String suffix = "";

    private String marriedName = "";

//    private String prefix;
//    private String title;


    public static Name parseLastCommaFirstName(String str) {
        String[] names = str.split(",");
        if (names.length == 0) {
            throw new RuntimeException("Unexpected lastName, firstName format: It is blank" + str);
        } else if (names.length > 3) {
            throw new RuntimeException("Unexpected lastName, firstName format: It contains an extra comma: '" + str + "'");
        }
        Name name = new Name();
        name.setSurName(toNameCase(names[0].trim()));
        if (names.length >= 2) {
            String n = names[1];
            name.setFirstNames(toNameCase(extractFirstNames(n).trim()));
            name.setNickName(toNameCase(extractNickName(n).trim()));
            name.setMarriedName(toNameCase(extractMarriedName(n).trim()));
        }
        if (names.length == 3) {
            name.setSuffix(toNameCase(names[2]).trim());

        }
        return name;
    }

    private static String extractNickName(String s) {
        return extractTextBetween(s, "\"", "\"");
    }

    private static String extractMarriedName(String s) {
        return extractTextBetween(s, "[", "]");
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
                "[", "]");                              // remove marriedName
    }

    private static String toNameCase(String s) {
        // only change the case if already in all caps
        if (StringUtilities.isAllCaps(s)) {
            return StringUtilities.toCapitalizedCase(s);
        }
        // else leave in existing case. E.g. McGee should NOT be Mcgee
        return s;
    }

    public String getLastCommaFirst() {
        String key = surName + ", " + firstNames;
        if (nickName != null && !nickName.isEmpty()) key += " \"" + nickName + "\"";
        if (marriedName != null && !marriedName.isEmpty()) key += " [" + marriedName + "]";
        if (suffix != null && !suffix.isEmpty()) key += ", " + suffix;
        return key;
    }

    public static boolean isMergeAllowed(Name n1, Name n2) {
        // nickname, and married name don't matter
        boolean allowed = (n1.firstNames.equalsIgnoreCase(n2.firstNames) &&
                n1.surName.equalsIgnoreCase(n2.surName)) ||
                (n1.firstNames.equalsIgnoreCase(n2.firstNames) &&
                        (n1.surName.isBlank() || n2.surName.isBlank())) ||
                ((n1.firstNames.isBlank() || n2.firstNames.isBlank()) &&
                        n1.surName.equalsIgnoreCase(n2.surName));
        // don't allow if suffix is not the same
        if (allowed && !n1.suffix.isBlank() && !n2.suffix.isBlank() && !n1.suffix.equalsIgnoreCase(n2.suffix)) {
            System.out.println("Warning: The two names match in everything but the suffix. Name1:" + n1 + " Name2:" + n2);
            allowed = false;
        }
        return allowed;

    }

    public void mergeInName(Name n1) {
        mergeInNickName(n1);
        mergeInMarriedName(n1);
    }

    public void mergeInNickName(Name n1) {
        // merge nickname, and married name
        if (n1.nickName != null && !n1.nickName.isBlank()) {
            if (nickName != null && !nickName.isBlank() && !nickName.equalsIgnoreCase(n1.nickName)) {
                throw new RuntimeException("Both names have non-blank and non-equal nicknames. this:" + this + " n1:" + n1);
            }
            // else
            nickName = n1.nickName;
        } // else n1=blank, so no merge
    }

    public void mergeInMarriedName(Name n1) {
        // should do lambdas instead
        if (n1.marriedName != null && !n1.marriedName.isBlank()) {
            if (marriedName != null && !marriedName.isBlank()) {
                throw new RuntimeException("Both names have non-blank marriedNames. this:" + this + " n1:" + n1);
            }
            // else
            marriedName = n1.marriedName;
        } // else n1=blank, so no merge
    }

    public boolean isBlank() {
        return surName.isBlank() && firstNames.isBlank();
    }
}