package com.swansong.familytree;

import com.swansong.familytree.model.Name;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParseNameTest {

    @Test
    void testNameParseAndOutput() {
        // input, expected output
        Map<String, String> cases = Map.ofEntries(
                Map.entry("MAGNUSSON,", "Magnusson, "),
                Map.entry(" SWANSON  ,  GRANT  ", "Swanson, Grant"),
                Map.entry(" McGee ,  Bill ", "McGee, Bill"),
                Map.entry("SWANSON ,   ", "Swanson, "),
                Map.entry("  ,  Cynthia", ", Cynthia"),
                Map.entry("SWANSON, Carl Loyal William \"Loyal\"", "Swanson, Carl Loyal William \"Loyal\""),
                Map.entry(" Swanson  ,  Bill,  Jr.  ", "Swanson, Bill, Jr."),
                Map.entry(" Swanson  ,  William \"Billy\" ,  Jr.   ", "Swanson, William \"Billy\", Jr."),
                Map.entry("                 , Sandra", ", Sandra"),
                Map.entry(",Jana Lynn [Neuzil] * ", ", Jana Lynn * [Neuzil]"),
                Map.entry("McGEE, ANDREW SHANE", "McGEE, Andrew Shane"), // fix later
                Map.entry("SWANSON, SVEN WILHELM \"William\"", "Swanson, Sven Wilhelm \"William\"")
        );

        for (String input : cases.keySet()) {
            Name name = Name.parseLastCommaFirstName(input);
            String output = name.getLastCommaFirst();
            assertEquals(cases.get(input), output);
        }
    }
}
