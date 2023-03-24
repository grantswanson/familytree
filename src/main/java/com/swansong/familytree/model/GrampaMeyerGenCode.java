package com.swansong.familytree.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrampaMeyerGenCode {
    private String rawGenCode="";

    @Override
    public String toString() {
        return rawGenCode;
    }


    public static GrampaMeyerGenCode buildSelfCode(String genCode) {
        return new GrampaMeyerGenCode(removeLastNumber(genCode));
    }

    public static GrampaMeyerGenCode buildParentsCode(String genCode) {
        return new GrampaMeyerGenCode(removeLastChar(removeLastNumber(genCode)));
    }

    public static GrampaMeyerGenCode buildChildsCode(String genCode, int childNumber) {
        return new GrampaMeyerGenCode(appendLetter(genCode, childNumber));
    }

    private static String removeLastNumber(String s) {
        if (s != null && !s.isEmpty() &&
                Character.isDigit(s.charAt(s.length() - 1))) {
            return s.substring(0, s.length() - 1);
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
