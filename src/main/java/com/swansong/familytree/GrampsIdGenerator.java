package com.swansong.familytree;

import java.util.UUID;

public class GrampsIdGenerator {
    public static String generateGrampsId() {
        UUID uuid = UUID.randomUUID();
        String grampsId = "G" + uuid.toString().replaceAll("-", "").substring(0, 20);
        String checkChar = calculateCheckChar(grampsId);
        return grampsId + checkChar;
    }

    private static String calculateCheckChar(String grampsId) {
        int sum = 0;
        for (int i = 0; i < grampsId.length(); i++) {
            char c = grampsId.charAt(i);
            int val = Character.isLetter(c) ? Character.toUpperCase(c) - 'A' + 10 : c - '0';
            sum = (sum + val) % 37;
        }
        int checkVal = (37 - sum) % 37;
        char checkChar = checkVal < 10 ? (char) ('0' + checkVal) : (char) ('A' + checkVal - 10);
        return String.valueOf(checkChar);
    }
}

