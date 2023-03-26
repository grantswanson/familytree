package com.swansong.familytree.biz;

import com.swansong.familytree.csvinput.Row;
import com.swansong.familytree.model.GenCode;
import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;

import java.util.HashMap;
import java.util.Map;


public class PersonBuilder {

    public static Person buildMainPerson(Map<String, Person> individualMap, Row row) {
        String selfGenCode =GenCode.buildSelfCode(row.getGenCode());
        Person existingPerson = individualMap.get(selfGenCode);

        if(existingPerson==null) {
            // make new person
            existingPerson = PersonBuilder.buildMainPerson(row);
            individualMap.put(selfGenCode, existingPerson);
        } else {
            existingPerson.appendDebug(" Also indiv ln#:"+ row.getNumber());
        }
        return existingPerson;
    }

    // build the main person for this row
    private static Person buildMainPerson(Row row) {
        String name = row.getName();
        if (name == null || name.isBlank())
            return null;

        Person person = buildBasicPerson(name);
        // add more here
        person.setSourceLineNumber(row.getNumber());
        person.setGenCode(GenCode.buildSelfCode(row.getGenCode()));

        return person;
    }

    public static Person buildSpouse(Map<String, Person> individualMap, Row row) {
        Person existingSpouse = individualMap.get(GenCode.buildSpousesCode(row.getGenCode()));
        if(existingSpouse==null) {
            // make new person
            existingSpouse = PersonBuilder.buildSpouse(row);
            if(existingSpouse != null) {
                individualMap.put(existingSpouse.getGenCode(), existingSpouse);
            }
        } else {
            existingSpouse.appendDebug(" Also spouse ln#:"+ row.getNumber());
        }
        return existingSpouse;
    }

    private static Person buildSpouse(Row row) {
        String name = row.getSpouse();
        if (name == null || name.isBlank())
            return null;

        Person person = buildBasicPerson(name);
        // add more here
        person.setSourceLineNumber(row.getNumber());
        // can't know the spouse's gen code
        person.setGenCode(GenCode.buildSpousesCode(row.getGenCode()));

        return person;
    }
    public static Person buildFather(Row row) {
        String name = row.getFather();
        if (name == null || name.isBlank())
            return null;

        Person person = buildBasicPerson(name);
        // add more here
        person.setSourceLineNumber(row.getNumber());
        person.setGenCode(GenCode.buildOriginalParentsCode(row.getGenCode()));
        person.setGender(true);
        return person;
    }

    public static Person buildMother(Row row) {
        String name = row.getMother();
        if (name == null || name.isBlank())
            return null;

        Person person = buildBasicPerson(name);
        // add more here
        person.setSourceLineNumber(row.getNumber());
        person.setGenCode(GenCode.buildOriginalParentsCode(row.getGenCode()));
        person.setGender(false);
        return person;
    }



    public Person buildSpouseFather(Row row) {
        String name = row.getSpouseFather();
        if (name == null || name.isBlank())
            return null;

        Person person = buildBasicPerson(name);
        // add more here
        person.setSourceLineNumber(row.getNumber());
        // can't know the spouse's gen code, nor the parents of the spouse
        person.setGenCode("FatherInLawOf:" + row.getGenCode());

        return person;
    }

    public Person buildSpouseMother(Row row) {
        String name = row.getSpouseMother();
        if (name == null || name.isBlank())
            return null;

        Person person = buildBasicPerson(name);
        // add more here
        person.setSourceLineNumber(row.getNumber());
        // can't know the spouse's gen code, nor the parents of the spouse
        person.setGenCode("MotherInLawOf:" + row.getGenCode());

        return person;
    }

    public Map<String, Person> buildChildren(Row row) {
        Map<String, Person> personMap = new HashMap<>();

        String surName = Name.parseLastCommaFirstName(row.getName()).getSurName();
        final int maxChildren = 12;
        for (int i = 1; i <= maxChildren; i++) {
            String name = row.getChild(i);
            if (name != null && !name.isBlank()) {
                name = surName + ", " + name;
                Person person = buildBasicPerson(name);
                // add more here
                person.setSourceLineNumber(row.getNumber());
                person.setGenCode(GenCode.buildChildsCode(row.getGenCode(), i));

                personMap.putIfAbsent(person.getGenCode(), person);
            }
        }
        return personMap;
    }

    private static Person buildBasicPerson(String name) {
        if (name == null || name.isBlank())
            System.out.println("Warning: Expected name to be not null and not blank! It is '" + name + "'");

        Person person = new Person();
        person.setName(Name.parseLastCommaFirstName(name));
        return person;
    }



}

