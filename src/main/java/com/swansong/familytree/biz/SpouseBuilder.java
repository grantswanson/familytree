package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.model.GenCode;
import com.swansong.familytree.model.Person;

import java.util.Map;

public class SpouseBuilder {
    public static Person buildSpouse(Map<String, Person> individualMap, Row row) {
        Person existingSpouse = individualMap.get(GenCode.buildSpousesCode(row.getGenCode()));
        if (existingSpouse == null) {
            // make new person
            existingSpouse = buildSpouse(row);
            if (existingSpouse != null) {
                individualMap.put(existingSpouse.getGenCode(), existingSpouse);
            }
        } else {
            existingSpouse.appendDebug(" Also spouse ln#:" + row.getNumber());
        }
        return existingSpouse;
    }

    private static Person buildSpouse(Row row) {
        String name = row.getSpouse();
        if (name == null || name.isBlank())
            return null;

        Person person = PersonBuilder.buildBasicPerson(name);
        person.setSourceLineNumber(row.getNumber());
        person.setGenCode(GenCode.buildSpousesCode(row.getGenCode()));

        person.setDob(row.getSpouseDob());
        person.setPob(row.getSpousePob());
        person.setBaptismDate(row.getSpouseBaptismDate());
        person.setBaptismPlace(row.getSpouseBaptismPlace());
        person.setConfirmationDate(row.getSpouseConfirmationDate());
        person.setConfirmationPlace(row.getSpouseConfirmationPlace());
        person.setDeathDate(row.getSpouseDeathDate());
        person.setBurialPlace(row.getSpouseBurialPlace());

        return person;
    }
}
