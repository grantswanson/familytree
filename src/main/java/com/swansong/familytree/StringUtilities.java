package com.swansong.familytree;

import java.util.LinkedHashSet;
import java.util.Set;

public class StringUtilities {
    public static String toCapitalizedCase(String str) {
        if (str == null || str.isEmpty()) {
            return str; // return the original string if it's null or empty
        }

        // split the input string into words
        String[] words = str.split("\\s+");

        // capitalize the first letter of each word
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (word.length() > 1) {
                sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase());
            } else {
                sb.append(word.toUpperCase());
            }
            sb.append(" ");
        }

        // trim the extra space at the end and return the capitalized string
        return sb.toString().trim();
    }

    public static boolean isAllCaps(String input) {
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isLetter(c) && !Character.isUpperCase(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllLowerCase(String input) {
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isLetter(c) && Character.isUpperCase(c)) {
                return false;
            }
        }
        return true;
    }

    public static String removeALLTextBetween(String s, String begin, String end) {
        String n = extractTextBetween(s, begin, end);
        while (!n.isBlank()) {
            s = removeTextBetween(s, begin, end);
            n = extractTextBetween(s, begin, end);
        }
        return s;
    }

    public static String extractTextBetween(String s, String begin, String end) {
        int firstQuoteIndex = s.indexOf(begin);
        int secondQuoteIndex = s.indexOf(end, firstQuoteIndex + 1);
        if (firstQuoteIndex != -1 && secondQuoteIndex != -1) {
            return s.substring(firstQuoteIndex + 1, secondQuoteIndex);
        }
        return "";
    }

    public static String removeTextBetween(String s, String begin, String end) {
        int firstQuoteIndex = s.indexOf(begin);
        int secondQuoteIndex = s.indexOf(end, firstQuoteIndex + 1);
        if (firstQuoteIndex != -1 && secondQuoteIndex != -1) {
            return s.substring(0, firstQuoteIndex) + s.substring(secondQuoteIndex + 1).trim();
        }
        return s;
    }

    public static Set<String> extractAltNames(String input) {
        Set<String> result = new LinkedHashSet<>();
        String[] parts = input.split(" ");
        boolean foundAlt = false;
        for (String part : parts) {
            if (foundAlt && !part.isBlank()) {
                result.add(part.trim());
            } else if (part.equals("alt:")) {
                foundAlt = true;
            }
        }
        return result;
    }

    public static String extractStringBeforeAlt(String input) {
        int index = input.indexOf("alt:");
        if (index == -1) {
            return input;
        } else {
            return input.substring(0, index).trim();
        }
    }

    public static String toNameCase(String s) {
        // only change the case if already in all caps
        if (isAllCaps(s) || isAllLowerCase(s)) {
            return toCapitalizedCase(s);
        }
        // else leave in existing case. E.g. McGee should NOT be Mcgee
        return s;
    }

    public static String addCommaIfMissing(String name) {
        if (name == null) {
            return ",";
        } else if (name.contains(",")) {
            return name;
        } else {
            return "," + name;
        }
    }

    public static String removeAsterisk(String name) {
        if (name == null) {
            return "";
        }
        return name.replace("*", "").trim();
    }
}
