package com.swansong.familytree;

import com.swansong.familytree.model.GenCode;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
            String output  = GenCode.buildSelfCode(input);
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
            String output  = GenCode.buildSpousesCode(input);
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

    @Test
    void buildParent2CodeTest() {
        // input, expected output
        Map<String, String> cases = Map.of(
                "S", "",
                "SF", "S1",
                "SA1", "SA1parent",
                "SBB2", "SBB2parent",
                "SBBC", "SBB1",
                "SBB3C", "SBB3",
                "SBB2C2", "SBB2C2parent",
                "SBB2AC2", "SBB2AC2parent"
        );
        for (String input : cases.keySet()) {
            String output = GenCode.buildParent2Code(input);
            assertEquals(cases.get(input), output);
        }
    }
    @Test
    void buildChildsCodeTest() {
        // input, expected output
        Map<Map<String, Integer>, String> cases = Map.of(
                Collections.singletonMap("S", 1), "SA",
                Collections.singletonMap("SABC", 2), "SABCB",
                Collections.singletonMap("SA1B2C1", 3), "SA1B2C1C"
        );
        for (Map<String, Integer> singletonMap : cases.keySet()) {
            // Get the single entry in the map
            Map.Entry<String, Integer> input = singletonMap.entrySet().iterator().next();

            String output = GenCode.buildChildsCode(input.getKey(), input.getValue());
            assertEquals(cases.get(singletonMap), output);
        }
    }
}
