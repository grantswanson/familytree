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


    public static void mergeInParent(Name parentsName, Row row, Map<String, Person> individualMap, boolean isFather ) {
        if(parentsName.isBlank()) {
            return;
        }
        Person parent1 = individualMap.get(GenCode.buildParent1Code(row.getGenCode()));
        Person parent2 = individualMap.get(GenCode.buildParent2Code(row.getGenCode()));

        if (parent1 != null && Name.isMergeAllowed(parentsName, parent1.getName())) {
            parent1.getName().mergeInName(parentsName);
            parent1.setGenderToMale(isFather);
            addChild(parent1, row, individualMap);
        } else if (parent2 != null && Name.isMergeAllowed(parentsName, parent2.getName())) {
            parent2.getName().mergeInName(parentsName);
            parent2.setGenderToMale(isFather);
            addChild(parent2, row, individualMap);
        } else {
            System.out.println((isFather?"Father":"Mother")+" not found in main list. ln#:" + row.getNumber()+
                    "\n "+(isFather?"Father":"Mother")+":" + parentsName.getLastCommaFirst() +
                    "\n Found Parent1:" + (parent1 == null ? "null" : parent1.getName().getLastCommaFirst()) +
                    "\n Found Parent2:" + (parent2 == null ? "null" : parent2.getName().getLastCommaFirst()));
        }
    }

    private static void addChild(Person parent, Row row,Map<String, Person> individualMap ) {
        int num =  GenCode.getChildNumber(row.getGenCode());
        Person child = individualMap.get(row.getGenCode());
        parent.addChild(child, num);
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
    public Map<String, Person> buildChildren(Map<String, Person> individualMap,Row row) {
        return null;
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

