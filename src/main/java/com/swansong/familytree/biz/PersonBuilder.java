package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.model.GenCode;
import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;

import java.util.Map;


public class PersonBuilder {


    public static Person buildMainPerson(Map<String, Person> individualMap, Row row) {
        String selfGenCode = GenCode.buildSelfCode(row.getGenCode());
        Person existingPerson = individualMap.get(selfGenCode);

        if (existingPerson == null) {
            // make new person
            existingPerson = PersonBuilder.buildMainPerson(row);
            individualMap.put(selfGenCode, existingPerson);
        } else {
            existingPerson.appendDebug(" Also indiv ln#:" + row.getNumber());
        }
        return existingPerson;
    }

    private static Person buildMainPerson(Row row) {
        String name = row.getName();
        if (name == null || name.isBlank())
            return null;

        Person person = buildBasicPerson(name);
        person.setSourceLineNumber(row.getNumber());
        person.setGenCode(GenCode.buildSelfCode(row.getGenCode()));

        person.setDob(row.getDob());
        person.setPob(row.getPob());
        person.setBaptismDate(row.getBaptismDate());
        person.setBaptismPlace(row.getBaptismPlace());
        person.setConfirmationDate(row.getConfirmationDate());
        person.setConfirmationPlace(row.getConfirmationPlace());
        person.setDeathDate(row.getDeathDate());
        person.setBurialPlace(row.getBurialPlace());
        return person;
    }

    public static Person buildBasicPerson(String name) {
        if (name == null || name.isBlank())
            System.out.println("Warning: Expected name to be not null and not blank! It is '" + name + "'");

        Person person = new Person();
        person.setName(Name.parseLastCommaFirstName(name));
        return person;
    }


}

