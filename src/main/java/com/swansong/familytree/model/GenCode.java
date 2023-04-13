package com.swansong.familytree.model;

import com.swansong.familytree.biz.ChildBuilder;

public class GenCode {
    private static final String SPOUSES_MOTHER_CODE = "sm";
    private static final String SPOUSES_FATHER_CODE = "sf";

    private static final String UNRELATED_FATHER_CODE = "uf";
    private static final String UNRELATED_MOTHER_CODE = "um";

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
        // e.g. SA2A1 becomes SA, SAA1 becomes SA
        return removeLastNumber(removeLastChar(removeLastNumber(genCode)));
    }

    public static String buildParent2Code(String genCode) {
        // remove the number and char to indicate current generation.
        // add a number if there is not already one there
        // e.g. SA2A1 becomes SA2, SAA1 becomes SA1
        return addLastNumber(removeLastChar(removeLastNumber(genCode)));
    }

    public static String buildUnrelatedMothersCode(String genCode) {
        // e.g. We are starting from and unrelated child's code e.g. SA2a
        // add "uf" or "um" to indicate the husband or wife of SA2. E.g. SA2aum or SA2aum
        // e.g. SA2a becomes SA2aum, SA1a becomes SA1aum, SA2A1a becomes SA2A1aum
        // but there can also be related parents e.g. MAA becomes MAAum (which is really the same as MA1)
//        if(!Character.isLowerCase(genCode.charAt(genCode.length()-1))) {
//            String retVal = buildParent2Code(genCode); // e.g. MAA becomes MA1 (or should it be MA??
//            System.out.println("Unexpected genCode:" + genCode + " Returning:" + retVal + " We were supposed to be starting from and unrelated child's code (ending in lowercase)");
//            return retVal;
//        }
        return genCode + UNRELATED_MOTHER_CODE;
    }

    public static String buildUnrelatedFathersCode(String genCode) {
        // e.g. We are starting from and unrelated child's code e.g. SA2a
        // add "uf" or "um" to indicate the husband or wife of SA2. E.g. SA2auf or SA2aum
        // e.g. SA2a becomes SA2auf, SA1a becomes SA1auf, SA2A1a becomes SA2A1auf
        // but there can also be related parents e.g. MAA becomes MAAuf (which is really the same as MA1 or maybe MA?)

//        if(!Character.isLowerCase(genCode.charAt(genCode.length()-1))) {
//            String retVal =  buildParent2Code(genCode); // e.g. MAA becomes MA1 (or should it be MA??
//            System.out.println("Unexpected genCode:"+genCode+ " Returning:"+retVal+" We were supposed to be starting from and unrelated child's code (ending in lowercase)");
//            return retVal;
//        }
        return genCode + UNRELATED_FATHER_CODE;
    }

    public static String buildChildsCode(String genCode, int childNumber) {
        ChildBuilder.verifyChildNumber(childNumber);
        return appendLetter(genCode, childNumber);
    }

    /**
     * @return GenCode where final letter is in lowercase.
     */
    public static String buildUnRelatedChildsCode(String genCode, int childNumber) {
        String str = buildChildsCode(genCode, childNumber);
        return str.substring(0, str.length() - 1) + Character.toLowerCase(str.charAt(str.length() - 1));
    }

    /**
     * @return Base 1. Not base 0
     */
    public static int getChildNumber(String genCode) {
        int i = getLastLetterValue(removeLastNumber(genCode));
        ChildBuilder.verifyChildNumber(i);
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

    private static boolean isEndingWithNumber(String s) {
        return (s != null && !s.isEmpty() &&
                Character.isDigit(s.charAt(s.length() - 1)));
    }

    private static String removeLastNumber(String s) {
        if (isEndingWithNumber(s)) {
            return s.substring(0, s.length() - 1);
        }
        return s;
    }


    private static String addLastNumber(String s) {
        if (s != null && !s.isEmpty() &&
                !isEndingWithNumber(s)) {
            return s + "1";
        }
        return s;
    }

    /**
     * add or remove the 1 at the end of the code
     */
    public static String buildAltCode(String code) {
        if (code == null || code.isBlank()) {
            return code;
        } else if (isEndingWithNumber(code)) {
            if (code.endsWith("1")) {
                return removeLastNumber(code);
            } else {
                return code; // ends with 2 or 3 or other n#
            }
        } else {
            return addLastNumber(code);
        }
    }

    public static boolean isEqual(String s1, String s2) {
        return addLastNumber(s1).equals(addLastNumber(s2));
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


    public static boolean isUnrelated(String genCode) {
        return Character.isLowerCase(genCode.charAt(genCode.length() - 1));

    }


}
