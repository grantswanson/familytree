package com.swansong.familytree.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringUtilsTest {


    @ParameterizedTest
    @CsvSource({
            "'Hello world', 'Hello world', '',''",
            "'This is a long string that needs to be split into multiple substrings', 'This is a long string that needs to be split into multiple substrings', '', ''",
            "'This is a very long string that needs to be split into multiple substrings. It should only break on word boundaries.', 'This is a very long string that needs to be split into multiple substrings. It should only', 'break on word boundaries.', '', ''"
    })
    void testSplitString(String input, String expected1, String expected2, String expected3) {
        String[] strs = StringUtils.splitStringAlongWords(input, 93);
        assertEquals(expected1, strs[0]);
        if (strs.length > 1) {
            assertEquals(expected2, strs[1]);
        } else {
            assertEquals(expected2, "");
        }
        if (strs.length > 2) {
            assertEquals(expected3, strs[2]);
        } else {
            assertEquals(expected3, "");
        }
    }
}
