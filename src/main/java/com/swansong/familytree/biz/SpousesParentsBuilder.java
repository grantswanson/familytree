package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.data.MarriageMap;
import com.swansong.familytree.data.PersonMap;
import com.swansong.familytree.model.*;

import java.util.ArrayList;

public class SpousesParentsBuilder {
    public static void buildSpousesParentsMarriage(Row row, Person spouse) {
        boolean foundFather = false;
        boolean foundMother = false;
        Person spousesFather = findSpousesParent(row, true);
        if (spousesFather == null) {
            spousesFather = buildAndSaveSpousesParent(row, true);
        } else {
            foundFather = true;
        }
        if (spouse != null && spousesFather != null) {
            spouse.setFather(spousesFather);
        }

        Person spousesMother = findSpousesParent(row, false);
        if (spousesMother == null) {
            spousesMother = buildAndSaveSpousesParent(row, false);
        } else {
            foundMother = true;
        }
        if (spouse != null && spousesMother != null) {
            spouse.setMother(spousesMother);
        }


        if (foundFather && foundMother) {// existing people
            MarriageMerger.verifyExistingMarriage(spousesFather, spousesMother,
                    Source.SpousesParents, spouse, row);
        } else if (spousesMother != null || spousesFather != null) {
            Marriage marriage = MarriageBuilder.buildMarriage(spousesFather, spousesMother,
                    row, Source.SpousesParents);
            marriage.addChild(spouse, 1);
            marriage.addSource(Source.SpousesParents);
            MarriageMap.addMarriage(marriage);

        }
    }


    private static Person findSpousesParent(Row row, boolean isMale) {
        String name = isMale ? row.getSpouseFather() : row.getSpouseMother();
        String genCode = isMale ? GenCode.buildSpousesFatherCode(row.getGenCode()) :
                GenCode.buildSpousesMotherCode(row.getGenCode());

        Person existingSpousesParent = PersonMap.getPersonByGenCodeOrRawName(
                genCode, name);
        if (existingSpousesParent != null) {
            String str = isMale ? "SpousesFather" : "SpousesMother";
            existingSpousesParent.appendDebug(" Also " + str + " ln#:" + row.getNumber());
        }
        return existingSpousesParent;
    }


    private static Person buildAndSaveSpousesParent(Row row, boolean isMale) {
        // make new person
        Person spousesParent = buildSpousesParent(row, isMale);
        if (spousesParent != null) {
            PersonMap.savePerson(spousesParent);
        }
        return spousesParent;
    }

    private static Person buildSpousesParent(Row row, boolean isMale) {
        String name = isMale ? row.getSpouseFather() : row.getSpouseMother();
        if (name == null || name.isBlank() || Name.isOnlySurname(name))
            return null;

        Person person = PersonBuilder.buildBasicPerson(name);
        // add more here
        person.setSourceRow(row);
        person.addSource(Source.SpousesParents);

        person.setGenderToMale(isMale);
        if (isMale) {
            person.setGenCode(GenCode.buildSpousesFatherCode(row.getGenCode()));
        } else {
            person.setGenCode(GenCode.buildSpousesMotherCode(row.getGenCode()));
        }

        return person;
    }

    public static void buildSpousesParentsMarriage(ArrayList<Row> csvData) {
        for (Row row : csvData) {

            Person spouse = SpouseBuilder.findSpouse(row);

            buildSpousesParentsMarriage(row, spouse);

        }
    }
}
