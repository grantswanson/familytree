package com.swansong.familytree.model;

public class GenCode {
    public static final String SPOUSES_MOTHER_CODE = "m";
    public static final String SPOUSES_FATHER_CODE = "f";

    private GenCode() {
    }

    public static String buildSelfCode(String genCode) {
        // remove numbers (multiple marriages),
        return removeLastNumber(genCode);
    }

    public static String buildSpousesFatherCode(String genCode) {
        return buildSpousesCode(genCode) + SPOUSES_FATHER_CODE;
    }
    public static String buildSpousesMotherCode(String genCode) {
        return buildSpousesCode(genCode) + SPOUSES_MOTHER_CODE;
    }

    public static String buildSpousesCode(String genCode) {
        return addLastNumber(genCode);
    }

    public static String buildParent1Code(String genCode) {
        // remove the number and char to indicate current generation.
        // remove the number to remove multiple marriages
        // e.g. SA2A1 becomes SA
        return removeLastNumber(removeLastChar(removeLastNumber(genCode)));
    }

    public static String buildParent2Code(String genCode) {
        // remove the number and char to indicate current generation.
        // add a number if there is not already one there
        // e.g. SA2A1 becomes SA2
        return addLastNumber(removeLastChar(removeLastNumber(genCode)));
    }

    public static String buildChildsCode(String genCode, int childNumber) {
        return appendLetter(genCode, childNumber);
    }

    /**
     * @return Base 1. Not base 0
     */
    public static int getChildNumber(String genCode) {
        int i = getLastLetterValue(removeLastNumber(genCode));
        Child.verifyChildNumber(i);
        return i;
    }

    /**
     * @return Base 1. Not base 0
     */
    private static int getLastLetterValue(String input) {
        if (input == null || input.isEmpty()) {
            return -1;
        }
        char lastChar = input.charAt(input.length() - 1);
        if (Character.isLetter(lastChar)) {
            return Character.toUpperCase(lastChar) - 'A' + 1;
        } else {
            return -1;
        }
    }

    private static String removeLastNumber(String s) {
        if (s != null && !s.isEmpty() &&
                Character.isDigit(s.charAt(s.length() - 1))) {
            return s.substring(0, s.length() - 1);
        }
        return s;
    }

    private static String addLastNumber(String s) {
        if (s != null && !s.isEmpty() &&
                !Character.isDigit(s.charAt(s.length() - 1))) {
            return s + "1";
        }
        return s;
    }

    private static String removeLastChar(String s) {
        if (s != null && !s.isEmpty()) {
            return s.substring(0, s.length() - 1);
        }
        throw new RuntimeException("Expected at least on char. s=" + s);
    }

    /**
     * @param input The string to append to
     * @param num   BASE 1, not 0. The first child is 1, the second is 2, etc.
     * @return A new string with the letter appended.
     */
    private static String appendLetter(String input, int num) {

        if (num <= 0 || num > 26) {
            throw new IllegalArgumentException("Invalid num argument. Expected it to be >=1 && <=26 num: " + num);
        }
        if (input == null) {
            input = "";
        }
        char letter = (char) ('A' + num - 1);
        return input + letter;
    }
}
