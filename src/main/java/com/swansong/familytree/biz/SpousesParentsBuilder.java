package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.data.MarriageMap;
import com.swansong.familytree.data.PersonMap;
import com.swansong.familytree.model.GenCode;
import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;

import java.util.ArrayList;

public class SpousesParentsBuilder {
    public static Marriage buildSpousesParentsMarriage(Row row, Person spouse) {
        Person spousesFather = buildSpousesFather(row);
        if (spouse != null && spousesFather != null) {
            spouse.setFather(spousesFather);
        }
        Person spousesMother = buildSpousesMother(row);
        if (spouse != null && spousesMother != null) {
            spouse.setMother(spousesMother);
        }

        if (spousesMother != null || spousesFather != null) {
            Marriage marriage = MarriageBuilder.buildMarriage(spousesFather, spousesMother, row);
            marriage.addChild(spouse, 1);
            marriage.setSpousesParents(true);

            return marriage;
        }
        return null;
    }

    private static Person buildSpousesFather(Row row) {
        Person existingSpousesFather = PersonMap.getPersonByGenCodeOrRawName(
                GenCode.buildSpousesFatherCode(row.getGenCode()),
                row.getSpouseFather());


        if (existingSpousesFather == null) {
            // make new person
            existingSpousesFather = buildSpousesParent(row, row.getSpouseFather(), true);
            if (existingSpousesFather != null) {
                PersonMap.savePerson(existingSpousesFather);
            }
        } else {
            existingSpousesFather.appendDebug(" Also SpousesFather ln#:" + row.getNumber());
        }
        return existingSpousesFather;
    }

    private static Person buildSpousesMother(Row row) {
        Person existingSpousesMother = PersonMap.getPersonByGenCodeOrRawName(
                GenCode.buildSpousesMotherCode(row.getGenCode()),
                row.getSpouseMother());
        if (existingSpousesMother == null) {
            // make new person
            existingSpousesMother = buildSpousesParent(row, row.getSpouseMother(), false);
            if (existingSpousesMother != null) {
                PersonMap.savePerson(existingSpousesMother);
            }
        } else {
            existingSpousesMother.appendDebug(" Also SpousesMother ln#:" + row.getNumber());
        }
        return existingSpousesMother;
    }

    private static Person buildSpousesParent(Row row, String name, boolean isMale) {
        if (name == null || name.isBlank() || Name.isOnlySurname(name))
            return null;

        Person person = PersonBuilder.buildBasicPerson(name);
        // add more here
        person.setSourceRow(row);
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

            Person spouse = SpouseBuilder.lookupSpouse(row);

            Marriage spousesParentsMarriage = buildSpousesParentsMarriage(row, spouse);
            if (spousesParentsMarriage != null) {
                MarriageMap.addMarriage(spousesParentsMarriage);
            }
        }
    }
}
