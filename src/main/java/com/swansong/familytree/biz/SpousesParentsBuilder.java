package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.model.GenCode;
import com.swansong.familytree.model.Marriage;
import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpousesParentsBuilder {
    public static Marriage buildSpousesParentsMarriage(Map<String, Person> individualMap, Row row, Person spouse) {
        Person spousesFather = buildSpousesFather(individualMap, row);
        if (spouse != null && spousesFather != null) {
            spouse.setFather(spousesFather);
        }
        Person spousesMother = buildSpousesMother(individualMap, row);
        if (spouse != null && spousesMother != null) {
            spouse.setMother(spousesMother);
        }

        if (spousesMother != null || spousesFather != null) {

            List<Name> childrensNames = spouse != null ? List.of(spouse.getName()) : new ArrayList<>();
            Marriage marriage = MarriageBuilder.buildMarriage(spousesFather, spousesMother, childrensNames, row);
            marriage.setChildren(new Person[]{spouse});

            return marriage;
        }
        return null;
    }

    public static Person buildSpousesFather(Map<String, Person> individualMap, Row row) {
        Person existingSpousesFather = individualMap.get(GenCode.buildSpousesFatherCode(row.getGenCode()));

        if (existingSpousesFather == null) {
            // make new person
            existingSpousesFather = buildSpousesFather(row);
            if (existingSpousesFather != null) {
                individualMap.put(existingSpousesFather.getGenCode(), existingSpousesFather);
            }
        } else {
            existingSpousesFather.appendDebug(" Also SpousesFather ln#:" + row.getNumber());
        }
        return existingSpousesFather;
    }

    public static Person buildSpousesMother(Map<String, Person> individualMap, Row row) {
        Person existingSpousesMother = individualMap.get(GenCode.buildSpousesMotherCode(row.getGenCode()));
        if (existingSpousesMother == null) {
            // make new person
            existingSpousesMother = buildSpousesMother(row);
            if (existingSpousesMother != null) {
                individualMap.put(existingSpousesMother.getGenCode(), existingSpousesMother);
            }
        } else {
            existingSpousesMother.appendDebug(" Also SpousesMother ln#:" + row.getNumber());
        }
        return existingSpousesMother;
    }

    public static Person buildSpousesFather(Row row) {
        String name = row.getSpouseFather();
        if (name == null || name.isBlank() || Name.isOnlySurname(name))
            return null;

        Person person = PersonBuilder.buildBasicPerson(name);
        // add more here
        person.setSourceLineNumber(row.getNumber());
        person.setGenderToMale(true);
        person.setGenCode(GenCode.buildSpousesFatherCode(row.getGenCode()));

        return person;
    }

    public static Person buildSpousesMother(Row row) {
        String name = row.getSpouseMother();
        if (name == null || name.isBlank() || Name.isOnlySurname(name))
            return null;

        Person person = PersonBuilder.buildBasicPerson(name);
        // add more here
        person.setSourceLineNumber(row.getNumber());
        person.setGenderToMale(false);
        person.setGenCode(GenCode.buildSpousesMotherCode(row.getGenCode()));

        return person;
    }
}
