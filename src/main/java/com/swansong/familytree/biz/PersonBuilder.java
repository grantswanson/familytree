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
        return PersonMap.getPersonByGenCode(
                GenCode.buildSelfCode(row.getGenCode()));
        //row.getName());
    }

    public static BuildPersonResult buildMainPerson(Row row) {
        Person existingPerson = findMainPerson(row);

        if (existingPerson == null) { // find by name
            existingPerson = PersonMap.getPersonByNameKey(Name.parseFullName(row.getName()).toNameKey());
            if (!parentsMatch(row, existingPerson)) {
                existingPerson = null;
            }
            if (existingPerson != null) {
                System.out.println("Warn: ln#" + row.getNumber() + " name:" + row.getName() +
                        "\n       found BY NAME:" + existingPerson.getName().toFullName() +
                        "\n  expectedGenCode:" + GenCode.buildSelfCode(row.getGenCode()) +
                        "\n    foundGenCode :" + existingPerson.getGenCode()
                );
            } else {
//                System.out.println("Warn: ln#" + row.getNumber() + " name:" + row.getName() +
//                        "\n       NOT found BY NAME." +
//                        "\n  expectedGenCode:" + GenCode.buildSelfCode(row.getGenCode())
//                );
            }
        }

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

    private static boolean parentsMatch(Row row, Person foundPerson) {
        if (foundPerson == null) {
            return false;
        }
        String foundFather = foundPerson.getSourceRow().getFather();
        String foundMother = foundPerson.getSourceRow().getMother();
        String foundSpousesFather = foundPerson.getSourceRow().getSpouseFather();
        String foundSpousesMother = foundPerson.getSourceRow().getSpouseMother();
        String thisFather = row.getFather();
        String thisMother = row.getMother();
        String thisSpousesFather = row.getSpouseFather();
        String thisSpousesMother = row.getSpouseMother();
        if ((foundFather == null || foundFather.isEmpty()) && (foundMother == null || foundMother.isEmpty())) {
            System.out.println("Found person's parents = null");
            return true;
        } else if ((thisFather == null || thisFather.isEmpty()) && (thisMother == null || thisMother.isEmpty())) {
            System.out.println("This person's parents = null");
            return true;
        } else if (!Name.isEqual(thisFather, foundFather) &&
                !Name.isEqual(thisFather, foundSpousesFather) &&
                !Name.isEqual(thisSpousesFather, foundFather) &&
                !Name.isEqual(thisSpousesFather, foundSpousesFather) &&

                !Name.isEqual(thisMother, foundMother) &&
                !Name.isEqual(thisMother, foundSpousesMother) &&
                !Name.isEqual(thisSpousesMother, foundMother) &&
                !Name.isEqual(thisSpousesMother, foundSpousesMother)
        ) {
            System.out.println("Warn: ln#" + row.getNumber() + " Parents do NOT match! " +
                    "\n thisFather:" + thisFather +
                    "\n foundFather:" + foundFather +
                    "\n thisSpousesFather:" + thisSpousesFather +
                    "\n foundSpousesFather:" + foundSpousesFather +

                    "\n thisMother:" + thisMother +
                    "\n foundMother:" + foundMother +
                    "\n thisSpousesMother:" + thisSpousesMother +
                    "\n foundSpousesMother:" + foundSpousesMother
            );
            return false;
        } else {
            System.out.println("Info: ln#" + row.getNumber() + " Parents DO match! " +
                    "\n thisFather:" + thisFather +
                    "\n foundFather:" + foundFather +
                    "\n thisSpousesFather:" + thisSpousesFather +
                    "\n foundSpousesFather:" + foundSpousesFather +

                    "\n thisMother:" + thisMother +
                    "\n foundMother:" + foundMother +
                    "\n thisSpousesMother:" + thisSpousesMother +
                    "\n foundSpousesMother:" + foundSpousesMother);
            return true;
        }
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

