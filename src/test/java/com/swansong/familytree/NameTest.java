package com.swansong.familytree;

import com.swansong.familytree.model.Name;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class  NameTest {

    @Test
    void parseLastCommaFirstNameTest() {
        // input, expected output
        Map<String, String> cases = Map.ofEntries(
                Map.entry("MAGNUSSON, *", "Magnusson, "),
                Map.entry(" SWANSON  ,  GRANT * ", "Swanson, Grant"),
                Map.entry(" McGee ,  Bill ", "McGee, Bill"),
                Map.entry("SWANSON *,   ", "Swanson, "),
                Map.entry("  ,  Cynthia", ", Cynthia"),
                Map.entry("SWANSON, Carl Loyal William \"Loyal\"", "Swanson, Carl Loyal William \"Loyal\""),
                Map.entry(" Swanson  ,  Bill,  Jr. * ", "Swanson, Bill, Jr"),
                Map.entry(" Swanson  ,  William \"Billy *\" ,  Jr.   ", "Swanson, William \"Billy\", Jr"),
                Map.entry("                 , Sandra", ", Sandra"),
                Map.entry(",Jana Lynn [Neuzil*] * ", ", Jana Lynn [Neuzil]"),
                Map.entry("Burns, Sandra Kay (Peters)(Lane)", "Burns, Sandra Kay [Peters] [Lane]"),
                Map.entry("McGEE, ANDREW SHANE", "McGEE, Andrew Shane"), // fix later
                Map.entry(" SWANSON  ,  GRANT * alt: Grantt", "Swanson, Grant alt: Grantt"),
                Map.entry("SWANSON, SVEN WILHELM \"William\"", "Swanson, Sven Wilhelm \"William\"")
        );

        for (String input : cases.keySet()) {
            Name name = Name.parseLastCommaFirstName(input);
            String output = name.getLastCommaFirst();
            assertEquals(cases.get(input), output);
        }
    }

    @Test
    void parseLastCommaFirstNameExceptionTest() {
        assertThrows(IllegalArgumentException.class, () -> Name.parseLastCommaFirstName(null));
        assertThrows(IllegalArgumentException.class, () -> Name.parseLastCommaFirstName("smith, joe, jr, sr"));
    }

    @ParameterizedTest
    @MethodSource("isMergeAllowedData")
    void isMergeAllowedTest(String name1, String name2, boolean expected) {
        Name n1 = Name.parseLastCommaFirstName(name1);
        Name n2 = Name.parseLastCommaFirstName(name2);
        boolean output = Name.isMergeAllowed(n1, n2);
        assertEquals(expected, output);
    }

    static Stream<Arguments> isMergeAllowedData() {
        return Stream.of(     // name1, name2, expected output
                Arguments.of("MAGNUSSON,  ", "Magnusson,   ", true),
                Arguments.of("MAGNUSSON,  ", "Magnusson,   ", true),
                Arguments.of(" SWANSON  ,  GRANT  ", "Swanson, Grant", true),
                Arguments.of(" SWANSON  ,  GRANT  ", "Swanson, Grant,Jr.", false),
                Arguments.of("SWANSON, Carl Loyal William \"Loyal\"", "Swanson, Carl Loyal William \"Loyal\"", true),
                Arguments.of("SWANSON, Carl Loyal William \"Loyal\"", "Swanson, Carl Loyal William", true),
                Arguments.of(" Swanson  ,  Bill,  Jr.  ", "Swanson, Bill, Jr", true),
                Arguments.of(" Swanson  ,  Bill  ", "Swanson, Bill, Jr.", false),
                Arguments.of(" Swanson  ,  William \"Billy\" ,  Jr.   ", "Swanson, William \"Billy\", Jr.", true),
                Arguments.of(" Swanson  ,  William \"Billy\" ,  Jr.   ", "Swanson, W \"Billy\", Jr.", false),
                Arguments.of(" Swanson  ,  bill", "Swanson, ", true),
                Arguments.of(" Swanson  ,  ", "Swanson, bill", true),
                Arguments.of(" , bill  ", "Swanson, bill", true),
                Arguments.of(" Swanson  ,  William \"Billy\" ,  Jr.   ", "Swanson,  Billy, Jr.", false),
                Arguments.of(",Jana Lynn [Neuzil] * ", ", Jana Lynn * [Neuzil]", true),
                Arguments.of(",Jana Lynn * ", ", Jana Lynn * [Neuzil]", true),
                Arguments.of(",Jana Lynn [Neuzil] * ", ", Jana Lynn [Brian]* ", true),
                Arguments.of("                 , Sandra", ", Sandra", true),
                Arguments.of("MCGEE, ANDrEW SHAne", "McGEe, AnDrew ShAne", true),
                Arguments.of("SWANSON, SVEN WILHELM \"William\"", "Swanson, Sven Wilhelm \"William\"", true)
        );
    }

    @ParameterizedTest(name = "{index} mergeNames: Name1:{0}, Name2:{1}, Expected:{2}")
    @MethodSource("mergeNamesData")
    void mergeNamesTest(String name1Str, String name2Str, String expectedStr) {
        Name name1 = Name.parseLastCommaFirstName(name1Str);
        Name name2 = Name.parseLastCommaFirstName(name2Str);
        Name expected = Name.parseLastCommaFirstName(expectedStr);
        name1.mergeInName(name2);
        assertEquals(expected, name1);
    }

    static Stream<Arguments> mergeNamesData() {
        return Stream.of(     // name1, name2, expected output
//                Arguments.of("Smith, John alt: Jon ", "Smith, John alt: Jonny" , "Smith, John alt: Jon Jonny"),
                Arguments.of("Smith, John  ", "Smith, John alt: Jonny", "Smith, John alt:     Jonny"),
                Arguments.of("Smith, John alt: Jon ", "Smith, John ", "Smith, John alt: Jon"),

                Arguments.of("MAGNUSSON,  ", "Magnusson, Sven  ", "Magnusson, Sven"),
                Arguments.of("MAGNUSSON, Sven ", "Magnusson,  ", "Magnusson, Sven"),
                Arguments.of("MAGNUSSON, Sven ", "", "Magnusson, Sven"),
                Arguments.of("MAGNUSSON, Sven ", ",Sven  ", "Magnusson, Sven"),
                Arguments.of(", Sven ", "Magnusson,  ", "Magnusson, Sven"),
                Arguments.of("", "Magnusson, Sven \"STEVE\" [Marriedname], Jr.  ", "Magnusson, Sven \"Steve\" [Marriedname], Jr."),
                Arguments.of("Magnusson, Sven \"STEVE\" [Marriedname], Jr.  ", "", "Magnusson, Sven \"Steve\" [Marriedname], Jr."),
                Arguments.of("Mag, Sven [Marriedname]", "Mag, Sven [M]", "Mag, Sven [Marriedname] [M] "),
                Arguments.of("Magnusson, Sven \"STEVE\" [Marr], Jr.  ", "Magnusson, Sven \"Steve\" [Marriedname], Jr.", "Magnusson, Sven \"STEVE\" [Marr] [Marriedname], Jr"),
                Arguments.of("Meyer, Minnie E. [Fastenow]", ", Minnie E. [Fastenow]", "Meyer, Minnie E. [Fastenow]"),
                Arguments.of("Magnus, Sven \"STEVE\" [Marriedname], Jr.  ", "", "Magnus, Sven \"Steve\" [Marriedname], Jr.")
        );
    }

    @ParameterizedTest(name = "{index} mergeNames expecting Exception: Name1:{0}, Name2:{1}")
    @MethodSource("mergeNames_ExceptionData")
    void mergeNames_ExceptionTest(String name1Str, String name2Str) {
        Name name1 = Name.parseLastCommaFirstName(name1Str);
        Name name2 = Name.parseLastCommaFirstName(name2Str);
        assertThrows(RuntimeException.class, () -> name1.mergeInName(name2));

    }

    static Stream<Arguments> mergeNames_ExceptionData() {
        return Stream.of(     // name1, name2
                Arguments.of("MAGNUSSON,  ", "M, Sven  "),
                Arguments.of("M, Sven ", "Magnusson,  "),
                Arguments.of("MAGNUSSON, S ", ",Sven  "),
                Arguments.of(", Sven ", "Magnusson, S "),
                Arguments.of("Mag, Sven, jr  ", "Mag, Sven, sr  "),
                Arguments.of("Mag, Sven, sr ", "Mag, Sven, jr   "),
                Arguments.of("Mag, Sven \"ST\"", "Mag, Sven \"Steve\""),
                Arguments.of("Magnusson, Sven \"ST\" [Marriedname], Jr.  ", "Magnusson, Sven \"Steve\" [Marriedname], Jr."),
                Arguments.of("Magnu, S ", "MAGNUSSON, Sven ", "Magnu, S ", "Magnu, S "),
                Arguments.of("MAGNUSSON, Sven ", "Magnu, S ", "Magnusson, Sven")
        );
    }


    @ParameterizedTest
    @CsvSource({"'Jones, Bill', false", "'', true", "'  ', true",
            "',\" jimmy\"', false",
            "'\" jimmy\"', false",
            "',[smith]', false",
            "',, jr.', false"
    })
    void isBlankTest(String name, boolean expected) {
        Name n1 = Name.parseLastCommaFirstName(name);
        boolean output = n1.isBlank();
        assertEquals(expected, output);
    }

    @ParameterizedTest
    @CsvSource({"'', ','", "'Smith, John', 'Smith, John'", "'Jane', ',Jane'", "'Doe, John', 'Doe, John'", ", ','"})
    void addCommaIfMissingTest(String name, String expected) {
        assertEquals(expected, StringUtilities.addCommaIfMissing(name));
    }

    @ParameterizedTest
    @CsvSource({"'Smith, John * ', 'Smith, John'", "'Jane*', 'Jane'", "'Doe, Jill * [Roe]', 'Doe, Jill  [Roe]'", "'  *  ',''"})
    void removeAsteriskTest(String name, String expected) {
        assertEquals(expected, StringUtilities.removeAsterisk(name));
    }

    @ParameterizedTest
    @CsvSource({"' Smith,  John * ', 'Smith, John'", "' Jane*', ', Jane'", "' Doe ,  Jill * [Roe] ', 'Doe, Jill [Roe]'",
            "Carol, ', Carol'"})
    void extractChildrensNameTest(String name, String expected) {
        //noinspection DataFlowIssue
        assertEquals(expected, Name.extractChildrensName(name).getLastCommaFirst());
    }

    @Test
    void extractChildrensNameNullTest() {
        assertNull(Name.extractChildrensName("  *  "));
    }

    @ParameterizedTest
    @CsvSource({"'Smith,  Johan', 'Smith, John', true",
            "' jane', ', Janey', true",
            "'Bill', 'Billy', true",
            "'William', 'Will', false",
            "'William', 'Bill', false",
            "Jeffery, Jeffrey, true"})
    void areNamesPossiblyMisspelled_StringTest(String name1, String name2, String expected) {
        boolean similar = Name.areNamesPossiblyMisspelled(name1, name2, true);
        assertEquals(expected, Boolean.toString(similar));
    }


    @ParameterizedTest
    @CsvSource({
            "'Smith,  Johan', 'Smith, John', true",
            "', jane', ', Janey', true",
            "' Bill', ', Janey', false",  // no leading comma
            "' jane', ', Janey', false",  // no leading comma
            "' jane', ', jane', false",
            "'Bill', 'Billy', true",
            "'William', 'Will', false",
            "'William', 'Bill', false",
            "Jeffery, Jeffrey, true"})
    void areNamesPossiblyMisspelled_NameTest(String name1, String name2, String expected) {
        boolean similar = Name.areNamesPossiblyMisspelled(Name.parseLastCommaFirstName(name1),
                Name.parseLastCommaFirstName(name2));
        assertEquals(expected, Boolean.toString(similar));
    }

    @ParameterizedTest
    @CsvSource({"',Smith', false", "'Smith, Jones', false", "'Ware, ', true "})
    void isOnlySurnameTest(String name, String expected) {
        boolean b = Name.isOnlySurname(name);
        assertEquals(expected, Boolean.toString(b));
    }

    @ParameterizedTest
    @CsvSource({"'Smith, Lynn', 'Smith, linn', 'Smith, Lynn alt: Linn'",
            "'Meyer, Minnie E. * \"Minny\" [Fastenow], Jr alt: Min', ', Minne E. [Fastenow]', 'Meyer, Minnie E. \"Minny\" [Fastenow], Jr alt: Min Minne'",
            "'Tigges, Marie Emma Lina [Kracht] alt: Lena', 'Tigges, Marie Emma Lena [Kracht]','Tigges, Marie Emma Lina [Kracht] alt: Lena'",
            "'Tigges, Marie Emma Lina [Kracht] alt: Lena', 'Tigges, Marie Emma Leni [Kracht]','Tigges, Marie Emma Lina [Kracht] alt: Lena Leni'"

    })
    void mergeInMisspelledNameTest(String name1, String name2, String expected) {
        Name n1 = Name.parseLastCommaFirstName(name1);
        Name n2 = Name.parseLastCommaFirstName(name2);
        n1.mergeInMisspelledName(n2, 0, "Unit Test: mergeInMisspelledNameTest");
        String calculated = n1.getLastCommaFirst();
        assertEquals(expected, calculated);
    }

//    @Test
//    void test() {
//        String s= ",";
//        String [] names = s.split(",");
//        assertEquals(0,names.length);
//    }

}
