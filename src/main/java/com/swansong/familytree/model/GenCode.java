package com.swansong.familytree.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class GenCode {
    private GenCode() {};


    public static String buildSelfCode(String genCode) {
        // remove numbers (multiple marriages),
        return removeLastNumber(genCode);
    }

    public static String buildParentsIndividualCode(String genCode) {
        // remove the number and char to indicate current generation.
        // remove the number to remove multiple marriages
        return removeLastNumber(removeLastChar(removeLastNumber(genCode)));
    }
    public static String buildNewParentsCode(String genCode) {
        // remove the number and char to indicate current generation.
        // add a number if there is not already one there
        return addLastNumber(removeLastChar(removeLastNumber(genCode)));
    }
    public static String buildSpousesCode(String genCode) {
        return addLastNumber(genCode);
    }
    public static String buildChildsCode(String genCode, int childNumber) {
        return appendLetter(genCode, childNumber);
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
            return s+"1";
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
