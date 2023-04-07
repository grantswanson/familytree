package com.swansong.familytree.gedcom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public
class Individual {
    private int id;
    private String givenName;
    private String surname;
    private String birthDate;
    private String birthPlace;
    private String deathDate;
    private String deathPlace;
    private String confirmationDate;
    private String confirmationPlace;
    private String highSchoolGraduationDate;
    private String highSchoolGraduationPlace;
    private String occupation;
    private String note;

    public static Individual.IndividualBuilder testIndividualBuilder = Individual.builder()
            .id(1)
            .givenName("John")
            .surname("Doe")
            .birthDate("15 JAN 2000")
            .birthPlace("New York")
            .deathDate("15 JAN 2070")
            .deathPlace("Los Angeles")
            .confirmationDate("15 JAN 2015")
            .confirmationPlace("Chicago")
            .highSchoolGraduationDate("15 JAN 2018")
            .highSchoolGraduationPlace("Boston")
            .occupation("Software Engineer")
            .note("This is a test instance of the Individual class.");

    public static Individual testIndividual1 = testIndividualBuilder.build();
    public static Individual testIndividual2 = testIndividualBuilder
            .id(2)
            .givenName("Jane")
            .surname("Doe")
            .birthDate("15 JAN 2000")
            .build();
    public static Individual testIndividual3 = testIndividualBuilder
            .id(3)
            .givenName("Baby")
            .surname("Doe")
            .birthDate("15 JAN 2021")
            .build();
}