package com.swansong.familytree.translate;

import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PersonValidatorTest {

    @ParameterizedTest
    @CsvSource({
            "2000 Jan 01, 2001 Jan 01, 2002 Jan 01, 2018 Jun 01, 2050 Jan 01, false",
            "2000 Jan 01, 1999 Jan 01, 2002 Jan 01, 2018 Jun 01, 2050 Jan 01, true",
            "2000 Jan 01, 2001 Jan 01, 1999 Jan 01, 2018 Jun 01, 2050 Jan 01, true",
            "2000 Jan 01, 2001 Jan 01, 2002 Jan 01, 1999 Jun 01, 2050 Jan 01, true",
            "2000 Jan 01, 2001 Jan 01, 2002 Jan 01, 2018 Jun 01, 1999 Jan 01, true",
            "2000 Jan 01, null     , null     , null     , null     , false",
            "null     , null     , null     , null     , null     , false"
    })
    void testValidatePersonDates(String dob,
                                 String baptismDate,
                                 String confirmationDate,
                                 String highSchoolGradDate,
                                 String deathDate,
                                 boolean shouldThrow) {
        Person person = new Person();
        person.setName(Name.parseFullName("Doe, John"));
        person.setDob(dob);
        person.setBaptismDate(baptismDate);
        person.setConfirmationDate(confirmationDate);
        person.setHighSchoolGradDate(highSchoolGradDate);
        person.setDeathDate(deathDate);

        if (shouldThrow) {
            assertThrows(IllegalArgumentException.class,
                    () -> PersonValidator.validatePersonDates(person));
        } else {
            assertDoesNotThrow(() -> PersonValidator.validatePersonDates(person));
        }
    }
}
