package com.swansong.familytree.utils;

import java.util.*;
import java.util.stream.Collectors;

public class StringUtils {

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

    /*
     * If the name is in all caps, then make just the first letter uppercase.
     * However, if the 2nd letter or later is in lower case and there is an uppercase
     * letter after that, then leave the first uppercase letter in uppercase.
     * Only the first letter and the first uppercase letter should be in uppercase.
     * For example: MacDONALD becomes MacDonald, McHENRY becomes McHenry, SMITH becomes Smith
     */
    public static String toNameCase(String name) {
        if (name == null) {
            return null;
        }

        String str = Arrays.stream(name.split("\\s+"))
                .map(word -> StringUtils.toNameCaseSingleWord(word.trim()))
                .collect(Collectors.joining(" "));
        return str.trim().replace(" , ", ", ");
    }

    private static String toNameCaseSingleWord(String name) {

        StringBuilder formattedName = new StringBuilder();
        boolean firstUppercaseFound = false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (i == 0) {
                formattedName.append(Character.toUpperCase(c));
            } else if (!firstUppercaseFound && Character.isLowerCase(name.charAt(i - 1)) && Character.isUpperCase(c)) {
                formattedName.append(c);
                firstUppercaseFound = true;
            } else {
                formattedName.append(Character.toLowerCase(c));
            }

        }
        return formattedName.toString();
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

    public static Set<String> diff(String s1, String s2) {
        Set<String> str1 = new HashSet<>(Arrays.asList(toNameCase(s1).split("\\s+")));
        Set<String> str2 = new HashSet<>(Arrays.asList(toNameCase(s2).split("\\s+")));
        str2.removeAll(str1);
        return str2;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }

    public static String[] splitStringAlongWords(String input, int maxLength) {
        String[] words = input.split(" ");
        List<String> result = new ArrayList<>();
        StringBuilder currentSubstring = new StringBuilder();
        for (String word : words) {
            if (currentSubstring.length() + word.length() + 1 > maxLength) {
                result.add(currentSubstring.toString());
                currentSubstring.setLength(0);
            }
            if (currentSubstring.length() > 0) {
                currentSubstring.append(" ");
            }
            currentSubstring.append(word);
        }
        if (currentSubstring.length() > 0) {
            result.add(currentSubstring.toString());
        }
        return result.toArray(new String[0]);
    }
}
