package com.swansong.familytree;

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
}
