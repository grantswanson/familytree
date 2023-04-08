package com.swansong.familytree.data;

import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.NameKey;
import com.swansong.familytree.model.Person;

import java.util.*;
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
        currentPerson = individualMapByName.get(newPerson.getName().toNameKey().toString());
        if (currentPerson != null) {
            throw new RuntimeException("Error: Tried to add someone who is already there by name! cur:" +
                    currentPerson.toShortString() + " trying to add:" + newPerson.toShortString());
        }

        individualMapByGenCode.put(newPerson.getGenCode(), newPerson);
        individualMapByName.put(newPerson.getName().toNameKey().toString(), newPerson);
    }

    public static Person getPersonByGenCode(String genCode) {
        return individualMapByGenCode.get(genCode);
    }

    public static Person getPersonByNameKey(NameKey nameKey) {
        return individualMapByName.get(nameKey.toString());
    }

    @SuppressWarnings("unused")
    public static Person getPersonByGenCodeOrNameKey(String genCode, NameKey nameKey) {
        Person person = individualMapByGenCode.get(genCode);
        if (person == null) {
            person = individualMapByName.get(nameKey.toString());
        }
        return person;
    }

    public static Person getPersonByGenCodeOrRawName(String genCode, String rawName) {
        Person person = individualMapByGenCode.get(genCode);
        if (person == null && rawName != null && !rawName.isBlank()) {
            NameKey nameKey = Name.parseFullName(rawName).toNameKey();
            person = individualMapByName.get(nameKey.toString());
        }
        return person;
    }

    public static void printIndividualMap() {
        Comparator<Map.Entry<String, Person>> valueComparator = Comparator
                .comparing((Map.Entry<String, Person> e) -> e.getValue().getSourceRow().getNumber())
                .thenComparing(e -> e.getValue().getGenCode());

//        Comparator<Map.Entry<String, Person>> valueComparator = Comparator
//                .comparing((Map.Entry<String, Person> e) -> e.getValue().getName().toFullName());
        Map<String, Person> sortedPersonMap = individualMapByGenCode.entrySet()
                .stream()
                .sorted(valueComparator)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        System.out.println("\nPeople...");
        for (Map.Entry<String, Person> entry : sortedPersonMap.entrySet()) {
            Person person = entry.getValue();
            System.out.println(person.toFormattedString());
        }

        System.out.println("Total Count=" + (individualMapByGenCode.size()));

    }

    public static int count() {
        if (individualMapByGenCode.size() != individualMapByName.size()) {
            throw new RuntimeException("individual map sizes don't match. byGenCode:" + individualMapByGenCode.size() +
                    " byName:" + individualMapByName.size());
        }
        return individualMapByGenCode.size();

    }

    public static Collection<Person> getPersonList() {
        return individualMapByGenCode.values();
    }
}
