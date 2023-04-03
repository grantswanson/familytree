package com.swansong.familytree.biz;

import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PersonMap {
    private static final Map<String, Person> individualMapByGenCode = new HashMap<>();
    private static final Map<String, Person> individualMapByName = new HashMap<>();


    public static void savePerson(Person newPerson) {
        Person currentPerson = individualMapByGenCode.get(newPerson.getGenCode());
        if (currentPerson != null) {
            throw new RuntimeException("Error: Tried to add someone who is already there by genCode! cur:" +
                    currentPerson.toShortString() + " trying to add:" + newPerson.toShortString());
        }
        currentPerson = individualMapByName.get(newPerson.getName().toNameKey());
        if (currentPerson != null) {
            throw new RuntimeException("Error: Tried to add someone who is already there by name! cur:" +
                    currentPerson.toShortString() + " trying to add:" + newPerson.toShortString());
        }
        individualMapByGenCode.put(newPerson.getGenCode(), newPerson);
        individualMapByName.put(newPerson.getName().toNameKey(), newPerson);
    }

    //    private static void savePersonByName(Person newPerson) {
//    }
//    private static void savePersonByGenCode(Person newPerson) {
//    }
    public static Person getPersonByGenCode(String genCode) {
        return individualMapByGenCode.get(genCode);
    }

    public static Person getPersonByNameKey(String nameKey) {
        return individualMapByName.get(nameKey);
    }

    public static Person getPersonByGenCodeOrNameKey(String genCode, String nameKey) {
        Person person = individualMapByGenCode.get(genCode);
        if (person == null) {
            person = individualMapByName.get(nameKey);
        }
        return person;
    }

    public static Person getPersonByGenCodeOrRawName(String genCode, String rawName) {
        Person person = individualMapByGenCode.get(genCode);
        if (person == null && rawName != null && !rawName.isBlank()) {
            String nameKey = Name.parseFullName(rawName).toNameKey();
            person = individualMapByName.get(nameKey);
        }
        return person;
    }

    public static void printIndividualMap() {
        Comparator<Map.Entry<String, Person>> valueComparator = Comparator
                .comparing((Map.Entry<String, Person> e) -> e.getValue().getSourceRow().getNumber())
                .thenComparing(e -> e.getValue().getGenCode());
        Map<String, Person> sortedPersonMap = individualMapByGenCode.entrySet()
                .stream()
                .sorted(valueComparator)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        for (Map.Entry<String, Person> entry : sortedPersonMap.entrySet()) {
            Person person = entry.getValue();
            String selfStr = String.format("#%-2d %-7s %1s %-30.30s",
                    person.getSourceRow().getNumber(),
                    person.getGenCode(),
                    person.getGender(),
                    person.getName().toFullName()
            );

//            selfStr += String.format("%-11s %-15.15s %-11s %-15.15s %s %s %s %s %s %s ",
//
//                    person.getDob(),
//                    person.getPob(),
//                    person.getBaptismDate(),
//                    person.getBaptismPlace(),
//                    person.getConfirmationDate(),
//                    person.getConfirmationPlace(),
//                    person.getDeathDate(),
//                    person.getDeathPlace(),
//                    person.getBurialDate(),
//                    person.getBurialPlace()
//                    );
//            System.out.println(person);
            System.out.print(selfStr);
            System.out.print(person.spousesToString());
            System.out.print(person.parentsToString());

            System.out.println();

        }

        System.out.println("Total Count=" + (individualMapByGenCode.size()));
    }
}
