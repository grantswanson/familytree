package com.swansong.familytree.model;

import com.swansong.familytree.csvinput.Row;

import java.util.HashMap;
import java.util.Map;


public class PersonBuilder {

    // build the main person for this row
    public Map<String, Person> buildPrimaryPerson(Row row) {
        String name = row.getName();
        if(name ==null || name.isBlank())
            return new HashMap<>();

        Person person = buildBasicPerson(name);
        // add more here
        person.setGrandpaMeyerCode(GrampaMeyerGenCode.buildSelfCode(row.getGenCode()));

        return Map.of(person.getKey(), person);
    }
    public Map<String, Person>  buildFather(Row row) {
        String name = row.getFather();
        if(name ==null || name.isBlank())
            return new HashMap<>();

        Person person = buildBasicPerson(name);
        // add more here
        person.setGrandpaMeyerCode(GrampaMeyerGenCode.buildParentsCode(row.getGenCode()));

        return Map.of(person.getKey(), person);
    }
    public Map<String, Person>  buildMother(Row row) {
        String name = row.getMother();
        if(name ==null || name.isBlank())
            return new HashMap<>();

        Person person = buildBasicPerson(name);
        // add more here
        person.setGrandpaMeyerCode(GrampaMeyerGenCode.buildParentsCode(row.getGenCode()));

        return Map.of(person.getKey(), person);
    }
    public Map<String, Person> buildSpouse(Row row) {
        String name = row.getSpouse();
        if(name ==null || name.isBlank())
            return new HashMap<>();

        Person person = buildBasicPerson(name);
        // add more here
        // can't know the spouse's gen code
        person.setGrandpaMeyerCode(new GrampaMeyerGenCode("SpouseOf:"+row.getGenCode()));

        return Map.of(person.getKey(), person);
    }
    public Map<String, Person> buildSpouseFather(Row row) {
        String name = row.getSpouseFather();
        if(name ==null || name.isBlank())
            return new HashMap<>();

        Person person = buildBasicPerson(name);
        // add more here
        // can't know the spouse's gen code, nor the parents of the spouse
        person.setGrandpaMeyerCode(new GrampaMeyerGenCode("FatherInLawOf:"+row.getGenCode()));

        return Map.of(person.getKey(), person);
    }
    public Map<String, Person> buildSpouseMother(Row row) {
        String name = row.getSpouseMother();
        if(name ==null || name.isBlank())
            return new HashMap<>();

        Person person = buildBasicPerson(name);
        // add more here
        // can't know the spouse's gen code, nor the parents of the spouse
        person.setGrandpaMeyerCode(new GrampaMeyerGenCode("MotherInLawOf:"+row.getGenCode()));

        return Map.of(person.getKey(), person);
    }
    public Map<String, Person> buildChildren(Row row) {
        Map<String, Person> personMap = new HashMap<>();

        String surName = Name.parseLastCommaFirstName(row.getName()).getSurName();
        final int maxChildren=12;
        for (int i = 1; i <= maxChildren; i++) {
            String name = row.getChild(i);
            if (name != null && !name.isBlank()) {
                name = surName+", " + name;
                Person person = buildBasicPerson(name);
                // add more here
                person.setGrandpaMeyerCode(GrampaMeyerGenCode.buildChildsCode(row.getGenCode(), i));

                personMap.putIfAbsent(person.getKey(), person);
            }
        }
        return personMap;
    }
    private Person buildBasicPerson(String name) {
        if(name ==null || name.isBlank())
            System.out.println("Warning: Expected name to be not null and not blank! It is '"+name+"'");

        return Person.builder()
                .name(Name.parseLastCommaFirstName(name))
                .build();
    }

}

