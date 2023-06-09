package com.swansong.familytree.model;

import com.swansong.familytree.biz.ChildBuilder;
import com.swansong.familytree.utils.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class NameTest {

    @Test
    void parseFullNameTest() {
        // input, expected output
        Map<String, String> cases = Map.ofEntries(
                Map.entry("Robert Joseph, Jr.", ", Robert Joseph, Jr"),
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
                Map.entry("McGEE, ANDREW SHANE", "McGee, Andrew Shane"), // fix later
                Map.entry(" SWANSON  ,  GRANT * alt: Grantt", "Swanson, Grant alt: Grantt"),
                Map.entry("McLEOD, George Wesley {Garvey}, Jr.", "McLeod, George Wesley {Garvey}, Jr"),
                Map.entry("SWANSON, SVEN WILHELM \"William\"", "Swanson, Sven Wilhelm \"William\"")
        );

        for (String input : cases.keySet()) {
            Name name = Name.parseFullName(input);
            String output = name.toFullName();
            assertEquals(cases.get(input), output);
        }
    }

    @Test
    void parseLastCommaFirstNameExceptionTest() {
        assertThrows(IllegalArgumentException.class, () -> Name.parseFullName(null));
        assertThrows(IllegalArgumentException.class, () -> Name.parseFullName("smith, joe, jr, sr"));
    }

    @Test
    void toNameKeyTest() {
        // input, expected output
        Map<String, String> cases = Map.ofEntries(
                Map.entry("Robert Joseph, Jr.", ", Robert Joseph, Jr"),
                Map.entry("MAGNUSSON, *", "Magnusson, "),
                Map.entry(" SWANSON  ,  GRANT * ", "Swanson, Grant"),
                Map.entry(" McGee ,  Bill ", "McGee, Bill"),
                Map.entry("SWANSON *,   ", "Swanson, "),
                Map.entry("  ,  Cynthia", ", Cynthia"),
                Map.entry("SWANSON, Carl Loyal William \"Loyal\"", "Swanson, Carl Loyal William"),
                Map.entry(" Swanson  ,  Bill,  Jr. * ", "Swanson, Bill, Jr"),
                Map.entry(" Swanson  ,  William \"Billy *\" ,  Jr.   ", "Swanson, William, Jr"),
                Map.entry("                 , Sandra", ", Sandra"),
                Map.entry(",Jana Lynn [Neuzil*] * ", ", Jana Lynn"),
                Map.entry("Burns, Sandra Kay (Peters)(Lane)", "Burns, Sandra Kay"),
                Map.entry("McGEE, ANDREW SHANE", "McGee, Andrew Shane"), // fix later
                Map.entry(" SWANSON  ,  GRANT * alt: Grantt", "Swanson, Grant"),
                Map.entry("SWANSON, SVEN WILHELM \"William\"", "Swanson, Sven Wilhelm")
        );

        for (String input : cases.keySet()) {
            Name name = Name.parseFullName(input);
            String output = name.toNameKey().toString();
            assertEquals(cases.get(input), output);
        }
    }

    @ParameterizedTest
    @MethodSource("isMergeAllowedData")
    void isMergeAllowedTest(String name1, String name2, boolean expected) {
        Name n1 = Name.parseFullName(name1);
        Name n2 = Name.parseFullName(name2);
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
        Name name1 = Name.parseFullName(name1Str);
        Name name2 = Name.parseFullName(name2Str);
        Name expected = Name.parseFullName(expectedStr);
        name1.mergeInName(name2);
        assertEquals(expected, name1);
    }


    static Stream<Arguments> mergeNamesData() {
        return Stream.of(     // name1, name2, expected output
                Arguments.of("Smith, John alt: Jon ", "Smith, John alt: Jonny", "Smith, John alt: Jon Jonny"),
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
        Name name1 = Name.parseFullName(name1Str);
        Name name2 = Name.parseFullName(name2Str);
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
    @CsvSource({"'Jones, Bill', false",
            "'', true",
            "'  ', true",
            "',\" jimmy\"', false",
            "'\" jimmy\"', false",
            "',[smith]', false",
            "',, jr.', false"
    })
    void isBlankTest(String name, boolean expected) {
        Name n1 = Name.parseFullName(name);
        boolean output = n1.isBlank();
        assertEquals(expected, output);
    }

    @ParameterizedTest
    @CsvSource({"'', ','", "'Smith, John', 'Smith, John'", "'Jane', ',Jane'", "'Doe, John', 'Doe, John'", ", ','"})
    void addCommaIfMissingTest(String name, String expected) {
        assertEquals(expected, StringUtils.addCommaIfMissing(name));
    }

    @ParameterizedTest
    @CsvSource({"'Smith, John * ', 'Smith, John'", "'Jane*', 'Jane'", "'Doe, Jill * [Roe]', 'Doe, Jill  [Roe]'", "'  *  ',''"})
    void removeAsteriskTest(String name, String expected) {
        assertEquals(expected, StringUtils.removeAsterisk(name));
    }

    @ParameterizedTest
    @CsvSource({"' Smith,  John * ', 'Smith, John'", "' Jane*', ', Jane'", "' Doe ,  Jill * [Roe] ', 'Doe, Jill [Roe]'",
            "Carol, ', Carol'"})
    void extractChildrensNameTest(String name, String expected) {
        //noinspection
        assertEquals(expected, ChildBuilder.extractChildrensName(name).toFullName());
    }

    @Test
    void extractChildrensNameNullTest() {
        assertNull(ChildBuilder.extractChildrensName("  *  "));
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
        boolean similar = Name.areNamesPossiblyMisspelled(Name.parseFullName(name1),
                Name.parseFullName(name2));
        assertEquals(expected, Boolean.toString(similar));
    }

    @ParameterizedTest
    @CsvSource({"',Smith', false",
            "'Smith, Jones', false",
            "'Ware, ', true ",
            "'Stewart, William James, Jr,', false"})
    void isOnlySurnameTest(String name, String expected) {
        boolean b = Name.isOnlySurname(name);
        assertEquals(expected, Boolean.toString(b));
    }

    @ParameterizedTest
    @CsvSource({
            "'John Smith', true",
            "'Jane Doe', true",
            "', Doe', false",
            "'John', true",
            "'Rosenberg Sue', true"
    })
    public void hasSurnameTest(String input, boolean expectedOutput) {
        assertEquals(expectedOutput, Name.hasSurname(input));
    }

    @ParameterizedTest
    @CsvSource({"'Smith, Lynn', 'Smith, linn', 'Smith, Lynn alt: Linn'",
            "'Meyer, Minnie E. * \"Minny\" [Fastenow], Jr alt: Min', ', Minne E. [Fastenow]', 'Meyer, Minnie E \"Minny\" [Fastenow], Jr alt: Min Minne'",
            "'Tigges, Marie Emma Lina [Kracht] alt: Lena', 'Tigges, Marie Emma Lena [Kracht]','Tigges, Marie Emma Lina [Kracht] alt: Lena'",
            "'Tigges, Marie Emma Lina [Kracht] alt: Lena', 'Tigges, Marie Emma Leni [Kracht]','Tigges, Marie Emma Lina [Kracht] alt: Lena Leni'"

    })
    void mergeInMisspelledNameTest(String name1, String name2, String expected) {
        Name n1 = Name.parseFullName(name1);
        Name n2 = Name.parseFullName(name2);
        n1.mergeInMisspelledName(n2);
        String calculated = n1.toFullName();
        assertEquals(expected, calculated);
    }


    @ParameterizedTest
    @CsvSource({
            "'Smith,  John Brian', 'Smith, John Brian', 'Smith, John Brian'",
            "'Smith,  John Brian', 'Smith, John B.', 'Smith, John Brian'",
            "'Smith,  John Brian', 'Smith, John B', 'Smith, John Brian'",
            "'Smith,  John Brian', ', John B', 'Smith, John Brian'",
            "'Smith,  John Brian', ', John B.', 'Smith, John Brian'",
            "',  John B.', ', John Brian', ', John Brian'",
            "',  John B.', 'Smith, John Brian', 'Smith, John Brian'",
            "'Smith, Lynn alt: Linn', 'Smith, Lynn', 'Smith, Lynn alt: Linn'",
            "'Meyer, Minnie E. * \"Minny\" [Fastenow], Jr alt: Min', ', Minnie E. [Fastenow]', 'Meyer, Minnie E \"Minny\" [Fastenow], Jr alt: Min'",
            "'Tigges, Marie Emma Lina', 'Tigges, Marie Emma Lina [Kracht] alt: Lena','Tigges, Marie Emma Lina [Kracht] alt: Lena'"
    })
    void mergeStartsWithTest(String name1, String name2, String expected) {
        Name n1 = Name.parseFullName(name1);
        Name n2 = Name.parseFullName(name2);
        n1.mergeStartsWith(n2);
        String calculated = n1.toFullName();
        assertEquals(expected, calculated);
    }

    @ParameterizedTest
    @CsvSource({
            "'Smith,  John Brian', 'Smith, John Brian', true",
            "'Smith,  John Brian', 'Smith, John B.', true",
            "'Smith,  John Brian', 'Smith, John B', true",
            "'Smith,  John Brian', 'Smith, John Bri', true", // a bit strange, but ok
            "'Smith,  John Brian', 'Smith, John Big', false",
            "'Smith,  Jonny Brian', 'Smith, Jon B.', false",
            "'Smith,  John Brian', 'Smith, J. Brian', false",
            "'Smith,  John Brian', 'Meyer, John Brian', false",
            "'Smith,  John Brian', ', John B.', true",
            "',  John B.', ', John Brian', true"
    })
    void startsWithTest(String name1, String name2, String expected) {
        boolean similar = Name.parseFullName(name1).startsWith(
                Name.parseFullName(name2));
        assertEquals(expected, Boolean.toString(similar));
    }
//    @Test
//    void test() {
//        String s= ",";
//        String [] names = s.split(",");
//        assertEquals(0,names.length);
//    }

}
