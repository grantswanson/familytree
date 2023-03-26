package com.swansong.familytree.biz;

import com.swansong.familytree.csvinput.Row;
import com.swansong.familytree.model.GenCode;
import com.swansong.familytree.model.Name;
import com.swansong.familytree.model.Person;

import java.util.AbstractMap;
import java.util.Map;


public class PersonMerger {
    public enum MergeResults {
        USE_NOBODY, USE_EXISTING, USE_NEW
    }



    public static Map.Entry<MergeResults, Person> merge(Person existingPerson, Person newPerson, Row row,
                                                        Map<String, Person> individualMap, boolean isMale) {
        if (newPerson == null) {
            // no new person to create
            System.out.println("No new person to create. Name:"
                    + row.getName() + " ln#:" + row.getNumber());
            // happens on row 0 where mother is not defined...
            // and could happen anywhere a parent is not defined
            return new AbstractMap.SimpleEntry<>(MergeResults.USE_NOBODY, null);

        } else if (existingPerson == null) {
            // use new newPerson
            System.out.println("No existing person. " +
                    " ln#:" + row.getNumber() +
                    "\n newGenCode:" + newPerson.getGenCode() +
                    "\n new:" + newPerson.getName().getLastCommaFirst());
            setNewPersonGenCode(newPerson, row, isMale);
            individualMap.put(newPerson.getGenCode(), newPerson);
            // happens on row 0 where father IS defined, but was not listed before
            return new AbstractMap.SimpleEntry<>(MergeResults.USE_NEW, newPerson);

        } else { // neither person is null
            // name and code match
            if (isExactPersonMatch(existingPerson, newPerson)) {
                System.out.println("Both Match " +
                        "\n ln#:" + row.getNumber() +
                        "\n existingGenCode:" + existingPerson.getGenCode() +
                        "\n newGenCode:" + newPerson.getGenCode() +
                        "\n existingName:" + existingPerson.getName().getLastCommaFirst() +
                        "\n new:" + newPerson.getName().getLastCommaFirst());
                existingPerson.getName().mergeInNickName(newPerson.getName());
                existingPerson.getName().mergeInNickName(newPerson.getName());
                existingPerson.getName().mergeInMarriedName(newPerson.getName());
                existingPerson.setGender(isMale);
                return new AbstractMap.SimpleEntry<>(MergeResults.USE_EXISTING, existingPerson);

            } else if (isGenCodeMatch(existingPerson, newPerson)) { // gen match, name not match
                System.out.println("Gen match, name not match. " +
                        "\n ln#:" + row.getNumber() +
                        "\n existingGenCode:" + existingPerson.getGenCode() +
                        "\n newGenCode:" + newPerson.getGenCode() +
                        "\n existingName:" + existingPerson.getName().getLastCommaFirst() +
                        "\n new:" + newPerson.getName().getLastCommaFirst());
                // There is a parent that matches (gen code match, but it is a different parent)
                // the newPerson is not in the list of individuals,
                // so add a new person
                // E.g. the spouse of one of our primary individuals will need to be added.
                setNewPersonGenCode(newPerson, row, isMale);
                individualMap.put(newPerson.getGenCode(), newPerson);
                return new AbstractMap.SimpleEntry<>(MergeResults.USE_NEW, newPerson);

            } else if (isNameMatch(existingPerson, newPerson)) { // gen not match, name match
                System.out.println("Gen NOT match, name match. " +
                        "\n ln#:" + row.getNumber() +
                        "\n existingGenCode:" + existingPerson.getGenCode() +
                        "\n newGenCode:" + newPerson.getGenCode() +
                        "\n existingName:" + existingPerson.getName().getLastCommaFirst() +
                        "\n new:" + newPerson.getName().getLastCommaFirst());
                existingPerson.getName().mergeInNickName(newPerson.getName());
                existingPerson.getName().mergeInMarriedName(newPerson.getName());
                existingPerson.setGender(isMale);
                return new AbstractMap.SimpleEntry<>(MergeResults.USE_EXISTING, existingPerson);
            } else {
                System.out.println("Gen NOT match, name NOT match. " +
                        " ln#:" + row.getNumber() +
                        " existingGenCode:" + existingPerson.getGenCode() +
                        " newGenCode:" + newPerson.getGenCode() +
                        " existingName:" + existingPerson.getName() +
                        " new:" + newPerson.getName());
                // not the same code AND not the same name
                // the newPerson is not in the list of individuals, so add them
                // E.g. the spouse of one of our primary individuals will need to be added.
                setNewPersonGenCode(newPerson, row, isMale);
                individualMap.put(newPerson.getGenCode(), newPerson);
                return new AbstractMap.SimpleEntry<>(MergeResults.USE_NEW, newPerson);
            }
        }
    }

    public static boolean isExactPersonMatch(Person p1, Person p2) {
        return isGenCodeMatch(p1, p2) &&
                isNameMatch(p1, p2);
    }

    public static boolean isGenCodeMatch(Person p1, Person p2) {
        return p1.getGenCode().equals(p2.getGenCode());
    }

    public static boolean isNameMatch(Person p1, Person p2) {
        return Name.isMergeAllowed(p1.getName(), p2.getName());
    }

    private static void setNewPersonGenCode(Person p, Row row, boolean isMale) {
        String genCode = GenCode.buildSpousesParentsCode(row.getGenCode());

        if (isMale) {
            p.appendDebug("husbandOf: " + Name.parseLastCommaFirstName(row.getMother()).getLastCommaFirst() + ":" + genCode);
            p.setGenCode(genCode + "h");
        } else {
            p.appendDebug("wifeOf: " + Name.parseLastCommaFirstName(row.getFather()).getLastCommaFirst() + ":" + genCode);
            p.setGenCode(genCode + "w");
        }

    }
}

