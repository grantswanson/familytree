package com.swansong.familytree.gedcom;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GedcomUtilsTest {

    @ParameterizedTest
    @CsvSource({
            "'1 BIRT\n', '01 JAN 1900', 'New York', '1 BIRT\n2 DATE 01 JAN 1900\n2 PLAC New York\n'",
            "'1 DEAT\n', '01 JAN 1950', 'Los Angeles', '1 DEAT\n2 DATE 01 JAN 1950\n2 PLAC Los Angeles\n'",
            "'1 BIRT\n', , 'New York', '1 BIRT\n2 PLAC New York\n'",
            "'1 BIRT\n', '01 JAN 1900', , '1 BIRT\n2 DATE 01 JAN 1900\n'",
            "'1 BIRT\n', , , ''"
    })
    void testGetDateAndPlace(String str1, String date, String place, String expected) {
        assertEquals(expected, GedcomUtils.getDateAndPlace(str1, date, place));
    }

    @ParameterizedTest
    @CsvSource({
            "'1 NOTE %s', 'This is a note', '1 NOTE This is a note'",
            "'1 OCCU %s', 'Farmer', '1 OCCU Farmer'",
            "'1 NOTE %s', '', ''",
            "'1 NOTE %s', , ''"
    })
    void testGetIfNotNullOrBlank(String tag, String data, String expected) {
        assertEquals(expected, GedcomUtils.getIfNotNullOrBlank(tag, data));
    }

    @ParameterizedTest
    @CsvSource({
            "'1 NOTE %s', 'This is a very long note that exceeds the maximum line length of 255 characters. This is a very long note that exceeds the maximum line length of 255 characters. This is a very long note that exceeds the maximum line length of 255 characters. This is a very long note that exceeds the maximum line length of 255 characters.'"
    })
    void testGetIfNotNullOrBlankThrowsException(String tag, String data) {
        assertThrows(RuntimeException.class, () -> GedcomUtils.getIfNotNullOrBlank(tag, data));
    }
}
