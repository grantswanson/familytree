package com.swansong.familytree.translate;

import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonValidatorTest {

    @ParameterizedTest
    @CsvSource({
            "2000 Jan 01, 2001 Jan 01, 2002 Jan 01, 2018 Jun 01, 2050 Jan 01, 0",
            "2000 Jan 01, 1999 Jan 01, 2002 Jan 01, 2018 Jun 01, 2050 Jan 01, 1",
            "2000 Jan 01, 2001 Jan 01, 1999 Jan 01, 2018 Jun 01, 2050 Jan 01, 1",
            "2000 Jan 01, 2001 Jan 01, 2002 Jan 01, 1999 Jun 01, 2050 Jan 01, 1",
            "2000 Jan 01, 2001 Jan 01, 2002 Jan 01, 2018 Jun 01, 1999 Jan 01, 4",
            "2000 Jan 01, null     , null     , null     , null     , 0",
            "null     , null     , null     , null     , null     , 0"
    })
    void testValidatePersonDates(String dob,
                                 String baptismDate,
                                 String confirmationDate,
                                 String highSchoolGradDate,
                                 String deathDate,
                                 int expectedWarningsCount) {
        Person person = new Person();
        person.setName(Name.parseFullName("Doe, John"));
        person.setDob(dob);
        person.setBaptismDate(baptismDate);
        person.setConfirmationDate(confirmationDate);
        person.setHighSchoolGradDate(highSchoolGradDate);
        person.setDeathDate(deathDate);

        List<String> warnings = PersonValidator.getWarnings(person);
        //System.out.println("Warnings:"+warnings);
        assertEquals(expectedWarningsCount, warnings.size());

    }
}
