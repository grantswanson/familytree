package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.data.MarriageMap;
import com.swansong.familytree.data.PersonMap;
import com.swansong.familytree.model.*;

import java.util.ArrayList;


public class PersonBuilder {

    public static void buildMainPersonAndSpouse(ArrayList<Row> csvData) {
        // build all the primary people
        for (Row row : csvData) {
            BuildPersonResult buildMainPersonResult = buildMainPerson(row);
            Person mainPerson = buildMainPersonResult.getPerson();

            BuildPersonResult buildSpouseResult = SpouseBuilder.buildSpouse(row);
            Person spouse = buildSpouseResult.getPerson();


            if (!buildMainPersonResult.isNew() && !buildSpouseResult.isNew()) {
                MarriageMerger.verifyExistingMarriage(mainPerson, spouse,
                        MarriageSource.MainAndSpouse, null, row);
            } else if (spouse != null || ChildBuilder.extractChildrensNames(row).size() > 0) { // add a marriage if there are children
                if (spouse == null) {
                    System.out.println("Warn: building marriage for main person with no spouse. ln#" + row.getNumber());
                }
                Marriage marriage = MarriageBuilder.buildMarriage(mainPerson, spouse, row, MarriageSource.MainAndSpouse);
                MarriageBuilder.addRowDetails(marriage, row);
                MarriageMap.addMarriage(marriage);
            }

        }
    }

    public static Person findMainPerson(Row row) {
        return PersonMap.getPersonByGenCodeOrRawName(
                GenCode.buildSelfCode(row.getGenCode()),
                row.getName());
    }

    public static BuildPersonResult buildMainPerson(Row row) {
        Person existingPerson = findMainPerson(row);

        if (existingPerson == null) {
            // make new person
            existingPerson = PersonBuilder.buildMainPersonDetails(row);
            PersonMap.savePerson(existingPerson);
            return new BuildPersonResult(existingPerson, true);

        } else {
            existingPerson.appendDebug(" Also indiv ln#:" + row.getNumber());
        }
        return new BuildPersonResult(existingPerson, false);

    }

    private static Person buildMainPersonDetails(Row row) {
        String name = row.getName();
        if (name == null || name.isBlank())
            throw new RuntimeException("Unexpected data. Main person is null");

        Person person = buildBasicPerson(name);
        person.setSourceRow(row);
        person.setGenCode(GenCode.buildSelfCode(row.getGenCode()));

        person.setDob(row.getDob());
        person.setPob(row.getPob());
        person.setBaptismDate(row.getBaptismDate());
        person.setBaptismPlace(row.getBaptismPlace());
        person.setConfirmationDate(row.getConfirmationDate());
        person.setConfirmationPlace(row.getConfirmationPlace());
        person.setHighSchoolGradDate(row.getHsGradDate());
        person.setHighSchoolGradPlace(row.getHsGradPlace());
        person.setDeathDate(row.getDeathDate());
        person.setBurialPlace(row.getBurialPlace());
        person.setOccupation(row.getOccupation());
        person.setChildrenNotes(row.getChildrenNotes());
        for (int i = 1; i < Row.NUM_OF_NOTES; i++) {
            if (row.getNote(i) != null && !row.getNote(i).isBlank()) {
                person.addNote(row.getNote(i));
            }
        }
        return person;
    }

    public static Person buildBasicPerson(String name) {
        if (name == null || name.isBlank())
            System.out.println("Warning: Expected name to be not null and not blank! It is '" + name + "'");

        Person person = new Person();
        person.setName(Name.parseFullName(name));
        return person;
    }



}

