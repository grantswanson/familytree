package com.swansong.familytree;

import com.swansong.familytree.model.GenCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GenCodeTest {


    @Test
    void buildSelfCodeTest() {
        // input, expected output
        Map<String, String> cases = Map.of(
                "S", "S",
                "SA1", "SA",
                "SBB2", "SBB",
                "SA2A1", "SA2A",
                "SA2A", "SA2A"
        );
        for (String input : cases.keySet()) {
            String output = GenCode.buildSelfCode(input);
            assertEquals(cases.get(input), output);
        }
    }

    @Test
    void buildSpousesCodeTest() {
        // input, expected output
        Map<String, String> cases = Map.of(
                "S", "S1",
                "SA1", "SA1",
                "SBB2", "SBB2",
                "SA2A1", "SA2A1",
                "SA2A", "SA2A1"
        );
        for (String input : cases.keySet()) {
            String output = GenCode.buildSpousesCode(input);
            assertEquals(cases.get(input), output);
        }
    }

    @Test
    void buildParent1CodeTest() {
        // input, expected output
        Map<String, String> cases = Map.of(
                "S", "",
                "SF", "S",
                "SA1", "S",
                "SBB2", "SB",
                "SBB2C2", "SBB",
                "SBB2AC2", "SBB2A"
        );
        for (String input : cases.keySet()) {
            String output = GenCode.buildParent1Code(input);
            assertEquals(cases.get(input), output);
        }
    }

    @ParameterizedTest(name = "{index} buildParent2CodeTest input:{0} expected:{1}")
    @MethodSource("buildParent2CodeData")
    void buildParent2CodeTest(String input, String expected) {

        String output = GenCode.buildParent2Code(input);
        assertEquals(expected, output);
    }

    static Stream<Arguments> buildParent2CodeData() {
        return Stream.of(    // input, expected output
                Arguments.of("S", ""),
                Arguments.of("SF", "S1"),
                Arguments.of("SA1", "S1"),
                Arguments.of("SBB2", "SB1"),
                Arguments.of("SBBC", "SBB1"),
                Arguments.of("SBB3C", "SBB3"),
                Arguments.of("SBB2C2", "SBB2")
        );
    }

    @ParameterizedTest
    @CsvSource({"S, 1, SA", "SABC, 2, SABCB", "SA1B2C1, 3, SA1B2C1C", " ,1,A"})
    void buildChildsCodeTest(String genCode, int num, String expected) {
        String output = GenCode.buildChildsCode(genCode, num);
        assertEquals(expected, output);

    }


    @ParameterizedTest
    @CsvSource({"SA, 1", "SB,2", "SA1, 1", "SA2, 1", "SAAAC1, 3", ",-1", "333, -1"})
    void getChildNumberTest(String genCode, int expected) {
        assertEquals(expected, GenCode.getChildNumber(genCode));
    }

    @Test
    void buildParent1CodeExceptionTest() {
        assertThrows(RuntimeException.class, () -> GenCode.buildParent1Code(null));
        assertThrows(RuntimeException.class, () -> GenCode.buildParent1Code(""));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void buildChildsCodeExceptionTest() {
        assertThrows(IllegalArgumentException.class, () -> GenCode.buildChildsCode("SA", 27));
        assertThrows(IllegalArgumentException.class, () -> GenCode.buildChildsCode("SA", 54982));
        assertThrows(IllegalArgumentException.class, () -> GenCode.buildChildsCode("SA", 0));
        assertThrows(IllegalArgumentException.class, () -> GenCode.buildChildsCode("SA", -3));
    }
}
