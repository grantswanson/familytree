package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.model.GenCode;
import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;


public class PersonBuilder {
    public static Person lookupMainPerson(Row row) {
        return PersonMap.getPersonByGenCodeOrRawName(
                GenCode.buildSelfCode(row.getGenCode()),
                row.getName());
    }

    public static Person buildMainPerson(Row row) {
        Person existingPerson = lookupMainPerson(row);

        if (existingPerson == null) {
            // make new person
            existingPerson = PersonBuilder.buildMainPersonDetails(row);
            PersonMap.savePerson(existingPerson);

        } else {
            existingPerson.appendDebug(" Also indiv ln#:" + row.getNumber());
        }
        return existingPerson;
    }

    private static Person buildMainPersonDetails(Row row) {
        String name = row.getName();
        if (name == null || name.isBlank())
            return null;

        Person person = buildBasicPerson(name);
        person.setSourceRow(row);
        person.setGenCode(GenCode.buildSelfCode(row.getGenCode()));

        person.setDob(row.getDob());
        person.setPob(row.getPob());
        person.setBaptismDate(row.getBaptismDate());
        person.setBaptismPlace(row.getBaptismPlace());
        person.setConfirmationDate(row.getConfirmationDate());
        person.setConfirmationPlace(row.getConfirmationPlace());
        person.setDeathDate(row.getDeathDate());
        person.setBurialPlace(row.getBurialPlace());

        person.setChildrenNotes(row.getChildrenNotes());
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

