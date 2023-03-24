package com.swansong.familytree;

import com.swansong.familytree.model.GrampaMeyerGenCode;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GrandpaMeyerGenCodeTest {


    @Test
    void buildSelfCodeTest() {
        // input, expected output
        Map<String, String> cases = Map.of(
                "S", "S",
                "SA1", "SA",
                "SBB2", "SBB"
        );
        for (String input : cases.keySet()) {
            GrampaMeyerGenCode code = GrampaMeyerGenCode.buildSelfCode(input);
            String output = code.toString();
            assertEquals(cases.get(input), output);
        }
    }

    @Test
    void buildParentsCodeTest() {
        // input, expected output
        Map<String, String> cases = Map.of(
                "S", "",
                "SA1", "S",
                "SBB2", "SB",
                "SBB2AC2", "SBB2A"
        );
        for (String input : cases.keySet()) {
            GrampaMeyerGenCode code = GrampaMeyerGenCode.buildParentsCode(input);
            String output = code.toString();
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

            GrampaMeyerGenCode code = GrampaMeyerGenCode.buildChildsCode(input.getKey(), input.getValue());
            String output = code.toString();
            assertEquals(cases.get(singletonMap), output);
        }
    }
}
