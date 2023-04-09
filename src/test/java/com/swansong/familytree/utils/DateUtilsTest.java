package com.swansong.familytree.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DateUtilsTest {
    @ParameterizedTest
    @CsvSource({
            "2022 Jan 01, 2022 Jan 01, false",
            "2022 Jan 01, 2022 Jan 02, true",
            "2022 Jan 02, 2022 Jan 01, false",
            "2022 Feb, 2022 Feb, false",
            "2022 Jan, 2022 Feb, true",
            "2022 Feb, 2022 Jan, false",
            "2022, 2023, true",
            "2023, 2022, false",
            "2022 Jan 01, 2023, true",
            "2022 Jan 01, 2022 Jan, false",
            "2022 Jan, 2022 Jan 01, false",
            "2021 Dec, 2022 Jan 01, true",
            "2021 Dec 31, 2022 Jan 01, true",
            "2022 Jan 01, 2022, false",
            "2022, 2022 Jan 01, false"
    })
    void testIsBefore(String date1, String date2, boolean expected) {
        assertEquals(expected, DateUtils.isBefore(date1, date2));
    }

    @ParameterizedTest
    @CsvSource({
            "2022 Jan 02, 2022 Jan 01, true",
            "2022 Jan 01, 2022 Jan 02, false",
            "2022 Feb, 2022 Jan, true",
            "2022 Jan, 2022 Feb, false",
            "2023, 2022, true",
            "2022, 2023, false",
            "2022 Jan, 2022 Jan 01, false",
            "2022 Jan 01, 2022 Jan, false",
            "2022, 2022 Jan 01, false",
            "2022 Jan 01, 2022, false"
    })
    void testIsAfter(String date1, String date2, boolean expected) {
        assertEquals(expected, DateUtils.isAfter(date1, date2));
    }

    @ParameterizedTest
    @CsvSource({
            "2022, true",
            "0, false",
            "-1, false",
            "not a year, false"
    })
    void testIsValidYear(String year, boolean expected) {
        assertEquals(expected, DateUtils.isValidYear(year));
    }

    @ParameterizedTest
    @CsvSource({
            "2022, Jan, 01, true",
            "2022, Jan, 32, false",
            "not a year, Jan, 01, false",
            "2022, not a month, 01, false",
            "2022, Jan, not a day, false"
    })
    void testIsValidDate(String year, String month, String day, boolean expected) {
        assertEquals(expected, DateUtils.isValidDate(year, month, day));
    }

    @ParameterizedTest
    @CsvSource({
            "null, ''",
            "'', ''",
            "'invalid date', ''",  // TODO Don't silently fail if an error
            "'2022', '2022'",
            "'2022 Jan', 'Jan 2022'",
            "'2022 Jan 01', '01 Jan 2022'"
    })
    void testConvertDate(String input, String expected) {
        assertEquals(expected, DateUtils.convertDate(input));
    }
}
