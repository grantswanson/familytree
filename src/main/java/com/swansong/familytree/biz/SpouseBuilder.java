package com.swansong.familytree.biz;

import com.swansong.familytree.csv.Row;
import com.swansong.familytree.model.GenCode;
import com.swansong.familytree.model.Person;

public class SpouseBuilder {
    public static Person lookupSpouse(Row row) {
        return PersonMap.getPersonByGenCodeOrRawName(
                GenCode.buildSpousesCode(row.getGenCode()),
                row.getSpouse());
    }

    public static Person buildSpouse(Row row) {
        Person existingSpouse = lookupSpouse(row);
        if (existingSpouse == null) {
            // make new person
            existingSpouse = buildSpouseDetails(row);
            if (existingSpouse != null) {
                PersonMap.savePerson(existingSpouse);
            }
        } else {
            existingSpouse.appendDebug(" Also spouse ln#:" + row.getNumber());
        }
        return existingSpouse;
    }

    private static Person buildSpouseDetails(Row row) {
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
