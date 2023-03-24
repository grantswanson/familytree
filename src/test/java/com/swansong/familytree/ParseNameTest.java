package com.swansong.familytree;

import com.swansong.familytree.model.Name;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Map;

public class ParseNameTest {

    @Test
    void testNameParseAndOutput() {
        // input, expected output
        Map<String, String> cases = Map.of(
                "MAGNUSSON,", "Magnusson, ",
                " SWANSON  ,  GRANT  ", "Swanson, Grant",
                " McGee ,  Bill ", "McGee, Bill",
                "SWANSON ,   ", "Swanson, ",
                "  ,  Cynthia", ", Cynthia",
                "SWANSON, Carl Loyal William \"Loyal\"", "Swanson, Carl Loyal William \"Loyal\"",
                " Swanson  ,  Bill,  Jr.  ", "Swanson, Bill, Jr.",
                " Swanson  ,  William \"Billy\" ,  Jr.   ", "Swanson, William \"Billy\", Jr.",
                "                 , Sandra", ", Sandra",
                ",Jana Lynn [Neuzil] * ", ", Jana Lynn * [Neuzil]"
                //"Jana Lynn [Neuzil] *", "Jana Lynn * [Neuzil]"


                );
        for (String input: cases.keySet() ) {
            Name name = Name.parseLastCommaFirstName(input);
            String output = name.getLastCommaFirst();
            assertEquals(cases.get(input), output);
        }
    }
}
